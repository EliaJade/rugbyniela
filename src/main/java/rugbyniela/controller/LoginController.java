package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.service.IAuthService;
import rugbyniela.service.IUserService;
import rugbyniela.service.UserServiceImp;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {

	private final IAuthService authService;
	private final UserServiceImp userService;
	private final ObjectMapper objectMapper;
	
	@PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UserResponseDTO> register(
			@RequestPart("user") String dto,
            @RequestPart(value = "file", required = false) MultipartFile file)throws JsonProcessingException {
		
			UserRequestDTO userRequest = objectMapper.readValue(dto, UserRequestDTO.class);
			UserResponseDTO response = userService.register(userRequest,file);
			return ResponseEntity.ok(response);
	}
	@PostMapping("/authenticate")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
		LoginResponseDTO dto = authService.login(loginRequestDTO);
		return ResponseEntity.ok(dto);
	}
	@PostMapping("/refresh-token")
	public ResponseEntity<LoginResponseDTO> refreshToken(
	    HttpServletRequest request
	) {
	    return ResponseEntity.ok(authService.refreshToken(request));
	}
}
