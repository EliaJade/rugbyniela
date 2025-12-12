package rugbyniela.service;

import org.antlr.v4.parse.ANTLRParser.throwsSpec_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
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
		//validations
		//unique
		if (userRepository.existsByEmail(dto.email())) { //without using JpaSpecificationExecutor<User>
	        throw new BusinessException("This email already belongs to a user");
	    }
		if(userRepository.existsByPhoneNumber(dto.phoneNumber())) {
			throw new BusinessException("This phone number already belongs to a user");
		}
		if(userRepository.existsByInstagram(dto.instagram())) {
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
