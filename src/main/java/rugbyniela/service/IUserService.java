package rugbyniela.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import rugbyniela.entity.dto.user.ChangePassworRequestDTO;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserCoalitionHistoryResponseDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;

public interface IUserService {

	UserResponseDTO register(UserRequestDTO dto, MultipartFile profilePicture);
	UserResponseDTO update(UserUpdatedRequestDTO dto);
	UserResponseDTO fetchUserById(Long id);
	Page<UserResponseDTO> fetchAllUsers(int page, Boolean isActive, String name);
	UserResponseDTO fetchCurrentUser();
	void changePassword(ChangePassworRequestDTO dto);
	//void recoveryAccount(); //if we have time we'll do this
	void registerInSeason(Long seasonId);
	UserSeasonScoreResponseDTO fetchSeasonPoints();
	UserResponseDTO updateUser(Long id, UserUpdatedRequestDTO updateRequest);
	void deleteUser(Long id);
	Page<UserCoalitionHistoryResponseDTO> fetchCoalitionUserHaveBeenRegistered(Pageable pageable);
	//esto va en notification service
//	void sendNotificationToUser();
//	void sendNotificationToUsers();
}
