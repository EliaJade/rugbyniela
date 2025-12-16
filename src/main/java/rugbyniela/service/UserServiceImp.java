package rugbyniela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.UserMapper;
import rugbyniela.repository.UserRepository;
import rugbyniela.security.JwtService;

@Service
public class UserServiceImp implements IUserService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
//	@Autowired
//	private  UserMapper userMapper;

	@Override
	public UserResponseDTO register(UserRequestDTO dto) {
		//
		return null;
	}

	@Override
	public UserResponseDTO update(UserUpdatedRequestDTO dto, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponseDTO fetchUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
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
	        return new LoginResponseDTO(jwtService.generateToken(securityUser));

	    } catch (BadCredentialsException e) {
	        throw new RugbyException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED, ActionType.AUTHENTICATION);
	    } catch (DisabledException e) {
	        throw new RugbyException("Cuenta desactivada", HttpStatus.FORBIDDEN, ActionType.AUTHENTICATION);
	    }
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
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
