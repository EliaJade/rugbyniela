package rugbyniela.service;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.Token;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.IAddressMapper;
import rugbyniela.mapper.IUserCoalitionHistoryMapper;
import rugbyniela.mapper.IUserMapper;
import rugbyniela.mapper.IUserSeasonScoreMaper;
import rugbyniela.repository.AddressRepository;
import rugbyniela.repository.SeasonRepository;
import rugbyniela.repository.TokenRepository;
import rugbyniela.repository.UserRepository;
import rugbyniela.repository.UserSeasonScoreRepository;
import rugbyniela.security.JwtService;
import rugbyniela.security.TokenValidator;
import rugbyniela.utils.StringUtils;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImp implements IUserService {

	private final UserRepository userRepository;
	private final UserSeasonScoreRepository userSeasonScoreRepository;
	private final SeasonRepository seasonRepository;
	private final IUserMapper userMapper;
	private final PasswordEncoder encoder;
	private final IUserCoalitionHistoryMapper historyMapper;
	private final IUserSeasonScoreMaper userSeasonScoreMaper;
	private final AddressRepository addressRepository;
	private final IAddressMapper addressMapper;

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
		String street = StringUtils.normalize(dto.address().street());
		String city = StringUtils.normalize(dto.address().city());
		String postalCode = StringUtils.normalize(dto.address().postalCode());
		String description = StringUtils.normalize(dto.address().description());
		Address address = addressRepository.findAddressByStreetAndCityAndPostalCodeAndDescription(street, city, postalCode, description)
				.orElseGet(()->{
					Address newAddress = addressMapper.toEntity(dto.address());
					return addressRepository.save(newAddress);
				});
		//mapping
		User user = userMapper.toEntity(dto);
		//encrypt password
		user.setPassword(encoder.encode(dto.password()));
		user.setActive(true);
		user.setAddress(address);
		//save
		userRepository.saveAndFlush(user); //works because userRepo implements JpaRepo 
		log.info("Usuario creado!");
		return userMapper.toDTO(user);
//		return null;
	}

	@Override
	public UserResponseDTO update(UserUpdatedRequestDTO dto) {
		//Find existing user
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		//partial update
		userMapper.updateUserFromDto(dto, user);
		User updatedUser = userRepository.save(user);
		return userMapper.toDTO(updatedUser);
//		return null;
	}

	@Override
	public UserResponseDTO fetchUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(()->new RugbyException("El usuario con id "+id+" no existe", HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION));
		return userMapper.toDTO(user);
//		return null;
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
	public void registerInSeason(Long seasonId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		Season season = seasonRepository.findByIdAndIsActiveTrue(seasonId).orElseThrow(() ->{ 
	    	throw new RugbyException("La temporada a la cual el usuario se quiere registrar no existe o ha terminado", HttpStatus.NO_CONTENT, ActionType.USER_ACTION);
	    });
		boolean isAlreadyRegistered = userSeasonScoreRepository.findByUserAndSeason(user, season).isPresent();
		if (isAlreadyRegistered) {
			throw new RugbyException("Ya estás registrado en esta temporada", HttpStatus.CONFLICT, ActionType.USER_ACTION);
		}
		UserSeasonScore newUserSeasonScore = new UserSeasonScore(
				null,
				0,
				new HashSet<>(),
				new HashSet<>(),
				true,
				season,
				user,
				user.getCurrentCoalition());
		
		userSeasonScoreRepository.save(newUserSeasonScore);
		log.info("Usuario {} registrado exitosamente en la temporada {}", user.getName(), season.getName());
		
	}

	@Override
	@Transactional
	public UserSeasonScoreResponseDTO fetchSeasonPoints() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		Season season = seasonRepository.findByIsActiveTrue().orElseThrow(() ->{ 
	    	throw new RugbyException("No hay una temporada en curso asi que el usuario no posee puntos", HttpStatus.NO_CONTENT, ActionType.USER_ACTION);
	    });
		UserSeasonScore userSeasonScore = userSeasonScoreRepository.findByUserAndSeason(user, season).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no esta registrado en la temporada actual", HttpStatus.NOT_FOUND, ActionType.USER_ACTION);
	    });
		return userSeasonScoreMaper.toDto(userSeasonScore);
	}

	@Override
	@Transactional
	public Page<UserCoalitionHistoryResponseDTO> fetchCoalitionUserHaveBeenRegistered(Pageable pageable) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		Page<UserSeasonScore> seasonScores = userSeasonScoreRepository.findByUser_EmailAndCoalitionIsNotNull(user.getEmail(), pageable);

		return seasonScores.map(historyMapper::toHistoryDTO);
	}

	@Override
	@Transactional
	public UserResponseDTO fetchCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName()).orElseThrow(() ->{ 
	    	throw new RugbyException("El usuario no existe", HttpStatus.NOT_FOUND, ActionType.AUTHENTICATION);
	    });
		return userMapper.toDTO(user);
//		return null;
	}

	


}
