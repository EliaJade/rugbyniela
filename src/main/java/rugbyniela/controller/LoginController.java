package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rugbyniela.entity.dto.user.LoginRequestDTO;
import rugbyniela.security.JwtService;

@RestController
@RequestMapping("/auth")
public class LoginController {

	
//	public ResponseEntity<?> login(LoginRequestDTO loginRequestDTO){
//		a
//	}
}
