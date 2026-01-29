package rugbyniela.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import rugbyniela.entity.dto.division.DivisionUpdateRequestDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.match.MatchUpdateRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayAddToDivisionRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.season.SeasonUpdateRequestDTO;
import rugbyniela.entity.dto.team.TeamAddToDivisionRequestDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.service.ICompetitiveService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CompetitiveController {

	private final ICompetitiveService competitiveService;

//	------------ALL-----------------	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/seasons")
	public ResponseEntity<Page<SeasonResponseDTO>> getSeasons(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			Authentication auth){
		
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchAllSeasons(page, isActive));
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/divisions")
	public ResponseEntity<Page<DivisionResponseDTO>> getDivisions(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchAllDivisions(page, isActive));
	}
	@PreAuthorize("isAuthenticated()")	
	@GetMapping("/match-days")
	public ResponseEntity<Page<MatchDayResponseDTO>> getMatchDays(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchAllMatchDays(page, isActive));
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/matches")
	public ResponseEntity<Page<MatchResponseDTO>> getMatches(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchAllMatches(page, isActive));
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/teams")
	public ResponseEntity<Page<TeamResponseDTO>> getTeams(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchAllTeams(page, isActive));
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
	
//	------------OTHER FETCHES-----------------	
	
	@GetMapping("/season/{id}/divisions")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<DivisionResponseDTO>> getDivisionsBySeason(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchDivisionsBySeason(id, page, isActive));
	}
	
	@GetMapping("/season/{seasonId}/division/{divisionId}")
    @PreAuthorize("isAuthenticated()")
	public ResponseEntity<DivisionResponseDTO> getDivisionsBySeasonAndId(@PathVariable Long seasonId, @PathVariable Long divisionId){
		
		return ResponseEntity.ok(competitiveService.fetchDivisionBySeasonAndId(seasonId, divisionId));
	}
	
	@GetMapping("/season/{id}/matches")
    @PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<MatchResponseDTO>> getMatchesBySeason(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchMatchesBySeason(id, page, isActive));
	}
	
	@GetMapping("/season/{id}/teams")
    @PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<TeamResponseDTO>> getTeamsBySeason(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchTeamsBySeason(id, page, isActive));
		
	}
	
	@GetMapping("/season/{seasonId}/division/{divisionId}/teams")
    @PreAuthorize("isAuthenticated()")
	public ResponseEntity<Page<TeamResponseDTO>> getTeamsBySeason(@PathVariable Long seasonId, @PathVariable Long divisionId, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Boolean isActive,
			Authentication auth){
		boolean isAdmin = auth != null && auth.getAuthorities().stream()
				.anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		if(!isAdmin) {
			isActive=true;
		}
		return ResponseEntity.ok(competitiveService.fetchTeamsBySeasonAndDivision(seasonId, divisionId, page, isActive));
		
	}
//	------------CREATE and ADD-----------------
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-team")
	public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO dto){
		TeamResponseDTO response = competitiveService.createTeam(dto);
		return ResponseEntity.ok(response);
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-team")
	public ResponseEntity<DivisionResponseDTO> addTeamToDivision(@Valid@RequestBody TeamAddToDivisionRequestDTO dto){
		DivisionResponseDTO response = competitiveService.addTeamToDivision(dto);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-match")
	public ResponseEntity<MatchResponseDTO> createMatch(@Valid @RequestBody MatchRequestDTO dto){
		MatchResponseDTO response = competitiveService.createMatch(dto);
		return ResponseEntity.ok(response);
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-match")
	public ResponseEntity<MatchDayResponseDTO> addMatchToMatchDay(@Valid @RequestBody MatchAddToMatchDayRequestDTO dto){
		MatchDayResponseDTO response = competitiveService.addMatchToMatchDay(dto);
		return ResponseEntity.ok(response);
		
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-match-day")
	public ResponseEntity<MatchDayResponseDTO> createMatchDay(@Valid@RequestBody MatchDayRequestDTO dto){
		MatchDayResponseDTO response = competitiveService.createMatchDay(dto);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-match-day")
	public ResponseEntity<DivisionResponseDTO> addMatchDayToDivision(@Valid@RequestBody MatchDayAddToDivisionRequestDTO dto){
		DivisionResponseDTO response = competitiveService.addMatchDayToDivision(dto);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-division")
	public ResponseEntity<DivisionResponseDTO> createDivision(@Valid@RequestBody DivisionRequestDTO dto){
		DivisionResponseDTO response = competitiveService.createDivision(dto);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-division")
	public ResponseEntity<SeasonResponseDTO> addDivisionToSeason(@Valid@RequestBody DivisionAddToSeasonRequestDTO dto){
		SeasonResponseDTO response = competitiveService.addDivisionToSeason(dto);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create-season")
	public ResponseEntity<SeasonResponseDTO> createSeason(@Valid@RequestBody SeasonRequestDTO dto){
		SeasonResponseDTO response = competitiveService.createSeason(dto);
		return ResponseEntity.ok(response);
		
	}
	
	
//	------------DELETE-----------------		 
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-season{id}")
	public ResponseEntity<SeasonResponseDTO> deleteSeason(@PathVariable Long id){
		competitiveService.deleteSeason(id);
		return ResponseEntity.noContent().build();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-division{id}")
	public ResponseEntity<DivisionResponseDTO> deleteDivision(@PathVariable Long id){
		competitiveService.deleteDivision(id);
		return ResponseEntity.noContent().build();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-match-day{id}")
	public ResponseEntity<MatchDayResponseDTO> deleteMatchDay(@PathVariable Long id){
		competitiveService.deleteMatchDay(id);
		return ResponseEntity.noContent().build();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-match{id}")
	public ResponseEntity<MatchRequestDTO> deleteMatch(@PathVariable Long id){
		competitiveService.deleteMatch(id);
		return ResponseEntity.noContent().build();
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete-team{id}")
	public ResponseEntity<TeamResponseDTO> deleteTeam(@PathVariable Long id){
		competitiveService.deleteTeam(id);
		return ResponseEntity.noContent().build();
	}
	
//	------------REMOVE-----------------	
//	@DeleteMapping("/remove-team{teamId}-from-match{matchId}")
//	public ResponseEntity<MatchResponseDTO> removeTeamFromMatch(@PathVariable Long teamId, @PathVariable Long matchId){
//		MatchResponseDTO response =competitiveService.removeTeamFromMatch(matchId, teamId);
//		return ResponseEntity.ok(response);
//	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/remove-team{teamId}-from-division{divisionId}")
	public ResponseEntity<DivisionResponseDTO> removeTeamFromDivision(@PathVariable Long teamId, @PathVariable Long divisionId){
		DivisionResponseDTO response= competitiveService.removeTeamFromDivision(teamId, divisionId);
		return ResponseEntity.ok(response);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/remove-match-day{matchDayId}-from-division{divisionId}")
	public ResponseEntity<DivisionResponseDTO> removeMatchDayFromDivision(@PathVariable Long matchDayId, @PathVariable Long divisionId){
		DivisionResponseDTO response= competitiveService.removeMatchDayFromDivision(matchDayId, divisionId);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/remove-match{matchId}-from-match-day{matchDayId}")
	public ResponseEntity<MatchDayResponseDTO> removeMatchFromMatchDay(@PathVariable Long matchId, @PathVariable Long matchDayId){
		MatchDayResponseDTO response= competitiveService.removeMatchFromMatchDay(matchId, matchDayId);
		return ResponseEntity.ok(response);
	}
	
	
//	------------UPDATE-----------------
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update-season{id}")
	public ResponseEntity<SeasonResponseDTO> updateSeason(@PathVariable Long id, @Valid@RequestBody SeasonUpdateRequestDTO dto){
		SeasonResponseDTO season = competitiveService.updateSeason(id, dto);
		return ResponseEntity.ok(season);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update-division{id}")
	public ResponseEntity<DivisionResponseDTO> updateDivision(@PathVariable Long id, @Valid@RequestBody DivisionUpdateRequestDTO dto){
		DivisionResponseDTO division = competitiveService.updateDivision(id, dto);
		return ResponseEntity.ok(division);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update-match-day{id}")
	public ResponseEntity<MatchDayResponseDTO> updateMatchDay(@PathVariable Long id, @Valid@RequestBody MatchDayRequestDTO dto){
		MatchDayResponseDTO matchDay = competitiveService.updateMatchDay(id, dto);
		return ResponseEntity.ok(matchDay);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update-match{id}")
	public ResponseEntity<MatchResponseDTO> updateMatch(@PathVariable Long id, @Valid@RequestBody MatchUpdateRequestDTO dto){
		MatchResponseDTO match = competitiveService.updateMatch(id, dto);
		return ResponseEntity.ok(match);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/update-team{id}")
	public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable Long id, @Valid@RequestBody TeamRequestDTO dto){
		TeamResponseDTO team = competitiveService.updateTeam(id, dto);
		return ResponseEntity.ok(team);
	}
}
















