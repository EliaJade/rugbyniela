package rugbyniela.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import rugbyniela.entity.dto.user.ChangePassworRequestDTO;
import rugbyniela.entity.dto.user.UserRequestDTO;
import rugbyniela.entity.dto.user.UserResponseDTO;
import rugbyniela.entity.dto.user.UserUpdatedRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserCoalitionHistoryResponseDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.service.UserServiceImp;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

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
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserResponseDTO> update(@Valid @RequestBody UserUpdatedRequestDTO dto){
		UserResponseDTO response = userService.update(dto);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
				return ResponseEntity.ok(userService.fetchUserById(id));
	}
	
	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserResponseDTO> fetchCurrentUser(){
		return ResponseEntity.ok(userService.fetchCurrentUser());
	}
	
	@PatchMapping("/change-password")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePassworRequestDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok().build();
    }
	
	@PostMapping("/register/season/{seasonId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> registerInSeason(@PathVariable Long seasonId){
		userService.registerInSeason(seasonId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping("/season-info")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserSeasonScoreResponseDTO> fetchSeasonPoints(){
		return ResponseEntity.ok(userService.fetchSeasonPoints());
	}
	
	@GetMapping("/history")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Page<UserCoalitionHistoryResponseDTO>> fetchHistory(
			@PageableDefault(size = 10, direction = Direction.ASC) Pageable pageable){
		return ResponseEntity.ok(userService.fetchCoalitionUserHaveBeenRegistered(pageable));
	}
	
}
