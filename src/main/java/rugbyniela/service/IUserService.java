package rugbyniela.service;

import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;

public interface IUserService {

	UserResponseDTO register(UserRequestDTO dto);
	UserResponseDTO update(UserUpdatedRequestDTO dto, Long id);
	UserResponseDTO fetchUserById(Long id);
	void changePassword();
	void recoveryAccount();
	LoginResponseDTO login(LoginRequestDTO loginRequestDTO);//TODO: this method possibly belong to security service
	void registerInSeason();
	void fetchSeasonPoints();
	void fetchSeasonUserHaveBeenRegistered();
	void registerInCoalition();
	void dismissOfCoalition();
	void fetchCoalitionUserHaveBeenRegistered();
	void sendNotificationToUser();
	void sendNotificationToUsers();
}
