package rugbyniela.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CompetitiveController {

	private final ICompetitiveService competitiveService;

//	------------ALL-----------------	
	
	@GetMapping("/seasons")
	public ResponseEntity<Page<SeasonResponseDTO>> getSeasons(@RequestParam(defaultValue = "0") int page){
		return ResponseEntity.ok(competitiveService.fetchAllSeasons(page));
	}
	
	@GetMapping("/divisions")
	public ResponseEntity<Page<DivisionResponseDTO>> getDivisions(@RequestParam(defaultValue = "0") int page){
		return ResponseEntity.ok(competitiveService.fetchAllDivisions(page));
	}
	
	@GetMapping("/match-days")
	public ResponseEntity<Page<MatchDayResponseDTO>> getMatchDays(@RequestParam(defaultValue = "0") int page){
		return ResponseEntity.ok(competitiveService.fetchAllMatchDays(page));
	}
	
	@GetMapping("/matches")
	public ResponseEntity<Page<MatchResponseDTO>> getMatches(@RequestParam(defaultValue = "0") int page){
		return ResponseEntity.ok(competitiveService.fetchAllMatches(page));
	}
	
	@GetMapping("/teams")
	public ResponseEntity<Page<TeamResponseDTO>> getTeams(@RequestParam(defaultValue = "0") int page){
		return ResponseEntity.ok(competitiveService.fetchAllTeams(page));
	}
	
	
//	------------BY ID-----------------
	
	@GetMapping("/season{id}")
	public ResponseEntity<SeasonResponseDTO> getSeasonById(@PathVariable Long id){
		return ResponseEntity.ok(competitiveService.fetchSeasonById(id));
	}
	
	@GetMapping("/division{id}")
	public ResponseEntity<DivisionResponseDTO> getDivisionById(@PathVariable Long id){
		return ResponseEntity.ok(competitiveService.fetchDivisionById(id));
	}
	
	@GetMapping("/match-day{id}")
	public ResponseEntity<MatchDayResponseDTO> getMatchDayById(@PathVariable Long id){
		return ResponseEntity.ok(competitiveService.fetchMatchDayById(id));
	}
	
	@GetMapping("/match{id}")
	public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable Long id){
		return ResponseEntity.ok(competitiveService.fetchMatchById(id));
	}
	
	@GetMapping("/team{id}")
	public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id){
		return ResponseEntity.ok(competitiveService.fetchTeamById(id));
	}
	
//	------------CREATE-----------------
	
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
















