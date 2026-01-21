package rugbyniela.service;

import jakarta.servlet.http.HttpServletRequest;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;

public interface IAuthService {

	LoginResponseDTO login(LoginRequestDTO loginRequestDTO);//TODO: this method possibly belong to security service
	LoginResponseDTO refreshToken(HttpServletRequest request);
}
