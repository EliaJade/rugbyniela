package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.entity.dto.user.LoginResponseDTO;
import rugbyniela.service.IAuthService;
import rugbyniela.service.IUserService;

@RestController
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	private IAuthService authService;
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO){
		LoginResponseDTO dto = authService.login(loginRequestDTO);
		return ResponseEntity.ok(dto);
	}
}
