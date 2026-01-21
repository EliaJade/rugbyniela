package rugbyniela.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rugbyniela.entity.dto.division.DivisionAddToSeasonRequestDTO;
import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.matchDay.MatchDayAddToDivisionRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.service.ICompetitiveService;

@RestController
@RequestMapping("/admin")
public class CompetitiveController {

	private ICompetitiveService competitiveService;

	public CompetitiveController(ICompetitiveService competitiveService) {
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
	
	@PostMapping("/createMatchDay")
	public ResponseEntity<MatchDayResponseDTO> createMatchDay(@Valid@RequestBody MatchDayRequestDTO dto){
		MatchDayResponseDTO response = competitiveService.createMatchDay(dto);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/addMatch")
	public ResponseEntity<MatchDayResponseDTO> addMatchToMatchDay(@Valid @RequestBody MatchAddToMatchDayRequestDTO dto){
		MatchDayResponseDTO response = competitiveService.addMatchToMatchDay(dto);
		return ResponseEntity.ok(response);
		
	}
	@PostMapping("/createDivision")
	public ResponseEntity<DivisionResponseDTO> createDivision(@Valid@RequestBody DivisionRequestDTO dto){
		DivisionResponseDTO response = competitiveService.createDivision(dto);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/addMatchDay")
	public ResponseEntity<DivisionResponseDTO> addMatchDayToDivision(@Valid@RequestBody MatchDayAddToDivisionRequestDTO dto){
		DivisionResponseDTO response = competitiveService.addMatchDayToDivision(dto);
		return ResponseEntity.ok(response);
	}	
	@PostMapping("/createSeason")
	public ResponseEntity<SeasonResponseDTO> createSeason(@Valid@RequestBody SeasonRequestDTO dto){
		SeasonResponseDTO response = competitiveService.createSeason(dto);
		return ResponseEntity.ok(response);
		
	}
	@PostMapping("/addDivision")
	public ResponseEntity<SeasonResponseDTO> addDivisionToSeason(@Valid@RequestBody DivisionAddToSeasonRequestDTO dto){
		SeasonResponseDTO response = competitiveService.addDivisionToSeason(dto);
		return ResponseEntity.ok(response);
	}
		 
	
}
















