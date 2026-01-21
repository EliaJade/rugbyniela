package rugbyniela.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
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
			UserResponseDTO response = userService.register(dto);
			return ResponseEntity.ok(response);
	}
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
				return ResponseEntity.ok(userService.fetchUserById(id));
	}
	
	//check endpoint works
	@GetMapping
	public ResponseEntity<String> test() {
	    return ResponseEntity.ok("User controller is working!");
	}


	
	
}
