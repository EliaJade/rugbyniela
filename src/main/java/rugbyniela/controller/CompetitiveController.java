package rugbyniela.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.service.CompetitiveService;

@RestController
@RequestMapping("/admin")
public class CompetitiveController {

	private CompetitiveService competitiveService;

	public CompetitiveController(CompetitiveService competitiveService) {
		super();
		this.competitiveService = competitiveService;
	}
	
	@PostMapping("/createTeam")
	public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO dto){
		TeamResponseDTO response = competitiveService.createTeam(dto);
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/createMatch")
	public ResponseEntity<MatchResponseDTO> createMatch(@Valid @RequestBody MatchRequestDTO dto){
		MatchResponseDTO response = competitiveService.createMatch(dto);
		return ResponseEntity.ok(response);
		
	}
	
	
}
