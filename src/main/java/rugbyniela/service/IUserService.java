package rugbyniela.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.servlet.http.HttpServletRequest;
import rugbyniela.entity.dto.user.ChangePassworRequestDTO;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserCoalitionHistoryResponseDTO;

public interface IUserService {

	UserResponseDTO register(UserRequestDTO dto);
	UserResponseDTO update(UserUpdatedRequestDTO dto);
	UserResponseDTO fetchUserById(Long id);
	UserResponseDTO fetchCurrentUser();
	void changePassword(ChangePassworRequestDTO dto);
	void recoveryAccount();
	void registerInSeason();
	void fetchSeasonPoints();
	void registerInCoalition();
	void dismissOfCoalition();
	List<UserCoalitionHistoryResponseDTO> fetchCoalitionUserHaveBeenRegistered();
	void sendNotificationToUser();
	void sendNotificationToUsers();
}
