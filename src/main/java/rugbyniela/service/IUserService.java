package rugbyniela.service;

import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.exceptions.BusinessException;

public interface IUserService {

	UserResponseDTO register(UserRequestDTO dto) throws BusinessException;
	UserResponseDTO update(UserUpdatedRequestDTO dto, Long id);
	UserResponseDTO fetchUserById(Long id);
	void changePassword();
	void recoveryAccount();
	void login();//TODO: this method possibly belong to security service
	void logout();//TODO: this method possibly belong to security service
	void registerInSeason();
	void fetchSeasonPoints();
	void fetchSeasonUserHaveBeenRegistered();
	void registerInCoalition();
	void dismissOfCoalition();
	void fetchCoalitionUserHaveBeenRegistered();
	void sendNotificationToUser();
	void sendNotificationToUsers();
}
