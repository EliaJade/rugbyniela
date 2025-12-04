package rugbyniela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.pojo.User;
import rugbyniela.mapper.UserMapper;
import rugbyniela.repository.UserRepository;

@Service
public class UserServiceImp implements IUserService {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  UserMapper userMapper;
	@Override
	public  void register() {
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAddress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAddress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAddress() {
		// TODO Auto-generated method stub

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

	@Override
	public void fetchUserById(Long id) {
		// TODO Auto-generated method stub
		
	}

}
