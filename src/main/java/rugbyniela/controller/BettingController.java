package rugbyniela.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreRequestDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketResponseDTO;
import rugbyniela.entity.pojo.UserSeasonScore;
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
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/tickets-by-user{id}")
	public ResponseEntity<Page<WeeklyBetTicketResponseDTO>> getAllUserSeasonTickets(@Valid @PathVariable Long id,  @RequestParam(defaultValue = "0") int page){
		Page<WeeklyBetTicketResponseDTO> response = bettingService.fetchUserSeasonTickets(id, page);
		return ResponseEntity.ok(response);
		
	}
	
	@PreAuthorize("hasRole('USER')")
	@PostMapping("/participate-season")
	public ResponseEntity<UserSeasonScoreResponseDTO>participateInSeason(@Valid @RequestBody UserSeasonScoreRequestDTO dto){
		UserSeasonScoreResponseDTO response = bettingService.participateInSeason(dto);
		return ResponseEntity.ok(response);
		
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/ticket-by-user{userSeasonId}-match-day{matchDayId}")
	public ResponseEntity<WeeklyBetTicketResponseDTO> fetchUserSeasonTicketByMatchDay(@PathVariable Long userSeasonId,@PathVariable Long matchDayId){
		WeeklyBetTicketResponseDTO response = bettingService.fetchUserSeasonTicketByMatchDay(userSeasonId, matchDayId);
		
		return ResponseEntity.ok(response);
		
	}
	

}
