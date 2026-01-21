package rugbyniela.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.mapper.AddressMapper;
import rugbyniela.mapper.IUserMapper;
import rugbyniela.repository.AddressRepository;
import rugbyniela.repository.TokenRepository;
import rugbyniela.repository.UserRepository;
import rugbyniela.utils.StringUtils;

@Service
@Slf4j
public class UserServiceImp implements IUserService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  IUserMapper userMapper;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private AddressMapper addressMapper;
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
	}

	@Override
	public UserResponseDTO update(UserUpdatedRequestDTO dto, Long id) {
		//Find existing user
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RugbyException ("Usuario no encontrado con id: " + id, HttpStatus.BAD_REQUEST, ActionType.AUTHENTICATION)); //might have to change actionType
		//partial update
		userMapper.updateUserFromDto(dto, user);
		User updatedUser = userRepository.save(user);
		return userMapper.toDTO(updatedUser);
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

	@Override
	public UserResponseDTO fetchCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	


}
