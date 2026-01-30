package rugbyniela.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.service.BettingServiceImp;

@RestController
@RequestMapping("/bet")
@RequiredArgsConstructor
public class BettingController {
	
	private final BettingServiceImp bettingService;
	
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/submit-ticket")
	public ResponseEntity<WeeklyBetTicketResponseDTO>submitTicket(@Valid @RequestBody WeeklyBetTicketRequestDTO dto){
		WeeklyBetTicketResponseDTO response = bettingService.submitTicket(dto);
		return ResponseEntity.ok(response);
		
		
	}
	

}
