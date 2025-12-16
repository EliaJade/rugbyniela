package rugbyniela.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.pojo.Gender;
import rugbyniela.entity.pojo.Role;
import rugbyniela.entity.pojo.User;
import rugbyniela.exceptions.BusinessException;
import rugbyniela.mapper.UserMapper;
import rugbyniela.repository.UserRepository;

@Service
public class UserServiceImp implements IUserService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  UserMapper userMapper;

	
	
	@Override
	public UserResponseDTO register(UserRequestDTO dto) throws BusinessException {
		
		//Normalize input
		String email = dto.email().trim().toLowerCase();
		String phone = dto.phoneNumber().trim();
		String instagram = dto.instagram() != null? dto.instagram().trim().toLowerCase() : null;
		
		//Convert GENDER safely
		Gender gender;
		try {
			gender = Gender.valueOf(dto.gender().trim().toUpperCase());
			
		}catch (IllegalArgumentException | NullPointerException ex) {
			throw new BusinessException("The gender options must be " + Arrays.toString(Gender.values()));
		}
		//Convert ROLE safely
		Role role;
	    try {
	        role = Role.valueOf(dto.role().trim().toUpperCase());
	    } catch (IllegalArgumentException | NullPointerException ex) {
	        throw new BusinessException("Invalid role");
	    }
		//validations
		//unique
		if (userRepository.existsByEmail(email)) { //without using JpaSpecificationExecutor<User>
	        throw new BusinessException("This email already belongs to a user");
	    }
		if(userRepository.existsByPhoneNumber(phone)) {
			throw new BusinessException("This phone number already belongs to a user");
		}
		if(instagram!= null && userRepository.existsByInstagram(instagram)) {
			throw new BusinessException("This instagram handle already belongs to a user");
		}
		if(dto.role()==null) {
			throw new BusinessException("You must assign a role to the user");
		}
		
		//mapping
		User user = userMapper.toEntity(dto);
		//encrypt password
		user.setActive(true);
		//save
		userRepository.save(user); //works because userRepo implements JpaRepo 
		return userMapper.toDTO(user);
	}

	@Override
	public UserResponseDTO update(UserUpdatedRequestDTO dto, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponseDTO fetchUserById(Long id) throws BusinessException {
		User user = userRepository.findById(id).orElseThrow(()->new BusinessException("User not found with id: " + id));
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
	public void login() {
		// TODO Auto-generated method stub
		
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
