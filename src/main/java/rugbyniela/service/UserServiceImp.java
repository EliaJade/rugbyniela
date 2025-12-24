package rugbyniela.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.Token;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Gender;
import rugbyniela.enums.Role;
import rugbyniela.enums.TokenType;
import rugbyniela.exception.RugbyException;

import rugbyniela.mapper.UserMapper;
import rugbyniela.repository.TokenRepository;
import rugbyniela.repository.UserRepository;
import rugbyniela.security.JwtService;

@Service
@Slf4j
public class UserServiceImp implements IUserService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private  UserMapper userMapper;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private TokenRepository tokenRepository;
	@Override
	public UserResponseDTO register(UserRequestDTO dto) {
		
		if (userRepository.existsByEmail(dto.email())) { //without using JpaSpecificationExecutor<User>
			log.warn("Intento fallido de registro. Email ya existe {}",dto.email());
			throw new RugbyException("Este email ya existe", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);
	    }
		if(userRepository.existsByPhoneNumber(dto.phoneNumber())) {
			log.warn("Intento fallido de registro. Numero de telefono ya existe {}",dto.phoneNumber());
			throw new RugbyException("Este numero de telefono ya existe", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);
		}
		if(dto.instagram()!= null && userRepository.existsByInstagram(dto.instagram())) {
			log.warn("Intento fallido de registro. Instagram ya existe {}",dto.instagram());
			throw new RugbyException("Este instagram ya existe", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);
		}
		//mapping
		User user = userMapper.toEntity(dto);
		//encrypt password
		user.setPassword(encoder.encode(dto.password()));
		user.setActive(true);
		//save
		userRepository.saveAndFlush(user); //works because userRepo implements JpaRepo 
		log.info("Usuario creado!");
		return userMapper.toDTO(user);
	}

	@Override
	public UserResponseDTO update(UserUpdatedRequestDTO dto, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponseDTO fetchUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(()->new RugbyException("El usuario con id "+id+" no existe", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION));
		return userMapper.toDTO(user);
	}

	@Override
	public void changePassword() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recoveryAccount() {
		// TODO Auto-generated method stub
		
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
	        // 4. Generamos el token usando ese usuario
	        String token = jwtService.generateToken(securityUser);
	        //we get the user in order to revoke its previous tokens
	        User user = securityUser.getUser();
	        revokeAllUserTokens(user);
	        saveUserToken(user, token);
	        return new LoginResponseDTO(token);

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
	private void saveUserToken(User user, String jwtToken) {
		Token token = Token.builder()
				.user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
		tokenRepository.save(token);
	}

	@Override
	public void registerInSeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchSeasonPoints() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchSeasonUserHaveBeenRegistered() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerInCoalition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dismissOfCoalition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchCoalitionUserHaveBeenRegistered() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendNotificationToUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendNotificationToUsers() {
		// TODO Auto-generated method stub
		
	}

	


}
