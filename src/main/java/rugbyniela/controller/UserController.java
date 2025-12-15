package rugbyniela.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.exceptions.BusinessException;
import rugbyniela.service.UserServiceImp;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserServiceImp userService;
	
	public UserController(UserServiceImp userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto) {
		try {
			UserResponseDTO response = userService.register(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (BusinessException e) {
			Map<String, String> errorBody = Map.of("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
	}
	
	//check endpoint works
	@GetMapping
	public ResponseEntity<String> test() {
	    return ResponseEntity.ok("User controller is working!");
	}


	
	
}
