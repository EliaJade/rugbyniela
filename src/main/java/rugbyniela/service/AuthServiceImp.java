package rugbyniela.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.Token;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.TokenType;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.TokenRepository;
import rugbyniela.repository.UserRepository;
import rugbyniela.security.JwtService;
import rugbyniela.security.TokenValidator;

@Service
public class AuthServiceImp implements IAuthService{

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private TokenValidator tokenValidator;
	
	@Override
	public LoginResponseDTO refreshToken(HttpServletRequest request) {
	    // 1. Extracción del Header (igual que antes)
	    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
	    final String refreshToken;
	    final String userEmail;

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        throw new RugbyException("No hay token", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION);
	    }

	    refreshToken = authHeader.substring(7);
	    userEmail = jwtService.extractUsername(refreshToken);

	    if (userEmail != null) {
	        // 2. Buscar usuario
	        User user = this.userRepository.findByEmail(userEmail)
	                .orElseThrow(() -> new RugbyException("Usuario no encontrado", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION));
	        
	        SecurityUser securityUser = new SecurityUser(user);

	        // 3. USO DEL VALIDATOR
	        // Aquí pasamos TokenType.REFRESH_TOKEN.
	        // Esto verifica: Firma OK + Expiración OK + No Revocado + ES REFRESH TOKEN
	        if (tokenValidator.isValid(refreshToken, securityUser, TokenType.REFRESH_TOKEN)) {
	            
	            // 4. Lógica de Rotación (Quemar el viejo, crear nuevos)
	            
	            // A) Revocamos el token que se acaba de usar
	            Token storedToken = tokenRepository.findByToken(refreshToken).orElseThrow();
	            storedToken.setRevoked(true);
	            storedToken.setExpired(true);
	            tokenRepository.save(storedToken);

	            // B) Generamos los nuevos
	            String newAccessToken = jwtService.generateToken(securityUser);
	            String newRefreshToken = jwtService.generateRefreshToken(securityUser);

	            // C) Guardamos los nuevos
	            saveUserToken(user, newAccessToken, TokenType.BEARER);
	            saveUserToken(user, newRefreshToken, TokenType.REFRESH_TOKEN);

	            return new LoginResponseDTO(newAccessToken, newRefreshToken);
	        }
	    }
	    
	    throw new RugbyException("Refresh token inválido o expirado", HttpStatus.FORBIDDEN, ActionType.AUTHENTICATION);
	}
	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
		// 1. Validaciones básicas
	    if (loginRequestDTO == null) {
	        throw new RugbyException("El dto para hacer login es null", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION);
	    }
	    
	    String email = loginRequestDTO.email();
	    String password = loginRequestDTO.password();
	    try {
	        // 2. Autenticación (Aquí Spring hace la Consulta a BD #1 y verifica password)
	        Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(email, password)
	        );

	        // 3. ¡OPTIMIZACIÓN! 
	        // En lugar de ir a la BD otra vez, sacamos el usuario del objeto 'authentication'
	        // Spring ya lo cargó en memoria, así que lo recuperamos con un casting.
	        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
	        // 4. Generamos el access token y el refresh token usando ese usuario
	        String accessToken = jwtService.generateToken(securityUser);
	        String refreshToken = jwtService.generateRefreshToken(securityUser);
	        //we get the user in order to revoke its previous tokens
	        User user = securityUser.getUser();
	        revokeAllUserTokens(user);
	        saveUserToken(user, accessToken,TokenType.BEARER);
	        saveUserToken(user, refreshToken,TokenType.REFRESH_TOKEN);
	        return new LoginResponseDTO(accessToken,refreshToken);

	    } catch (BadCredentialsException e) {
	    	
	        throw new RugbyException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED, ActionType.AUTHENTICATION);
	    } catch (DisabledException e) {
	        throw new RugbyException("Cuenta desactivada", HttpStatus.FORBIDDEN, ActionType.AUTHENTICATION);
	    }
	}
	
	private void revokeAllUserTokens(User user) {
		List<Token> tokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if(tokens.isEmpty()) {
			return;
		}
		tokens.forEach(token->{
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(tokens);
	}
	private void saveUserToken(User user, String jwtToken,TokenType type) {
		Token token = Token.builder()
				.user(user)
                .token(jwtToken)
                .tokenType(type)
                .expired(false)
                .revoked(false)
                .build();
		tokenRepository.save(token);
	}

}
