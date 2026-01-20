package rugbyniela.service;


import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.user.ChangePassworRequestDTO;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserCoalitionHistoryResponseDTO;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.Token;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Gender;
import rugbyniela.enums.Role;
import rugbyniela.enums.TokenType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.IUserCoalitionHistoryMapper;
import rugbyniela.mapper.IUserMapper;
import rugbyniela.repository.TokenRepository;
import rugbyniela.repository.UserRepository;
import rugbyniela.security.JwtService;
import rugbyniela.security.TokenValidator;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImp implements IUserService {

	
	private final UserRepository userRepository;
	private final IUserMapper userMapper;
	private final PasswordEncoder encoder;
	private final IUserCoalitionHistoryMapper historyMapper;

	
	
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
	public UserResponseDTO update(UserUpdatedRequestDTO dto) {
		//TODO: elia did this
		return null;
	}

	@Override
	public UserResponseDTO fetchUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(()->new RugbyException("El usuario con id "+id+" no existe", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION));
		return userMapper.toDTO(user);
	}

	@Override
	@Transactional
	public void changePassword(ChangePassworRequestDTO dto) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		
		if(!dto.currentPassword().equals(dto.confirmationPassword())) {
			throw new RugbyException("La nueva contraseña y la confirmación no coinciden", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION);
		}
		if(!encoder.matches(dto.newPassword(), user.getPassword())) {
			throw new RugbyException("La nueva contraseña no puede ser igual a la anterior", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION);
		}
		user.setPassword(encoder.encode(dto.newPassword()));
		userRepository.save(user);
		log.info("El usuario {} ha cambiado su contraseña exitosamente", user.getEmail());
	}

	@Override
	public void recoveryAccount() {
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
	public void registerInCoalition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dismissOfCoalition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public List<UserCoalitionHistoryResponseDTO> fetchCoalitionUserHaveBeenRegistered() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		Set<UserSeasonScore> seasonScores = user.getSeasonScores();

		if (seasonScores == null || seasonScores.isEmpty()) {
			return Collections.emptyList();
		}
		return seasonScores.stream()
				.filter(score -> score.getCoalition()!=null)
				.map(historyMapper::toHistoryDTO)
				.sorted(Comparator.comparing(UserCoalitionHistoryResponseDTO::seasonId).reversed())
				.toList();
	}

	@Override
	public void sendNotificationToUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendNotificationToUsers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Transactional
	public UserResponseDTO fetchCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		return userMapper.toDTO(user);
	}

	


}
