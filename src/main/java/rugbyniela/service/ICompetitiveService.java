package rugbyniela.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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

public interface ICompetitiveService {

	Page<SeasonResponseDTO> fetchAllSeasons(int page, Boolean isActive);
	SeasonResponseDTO fetchSeasonById(Long seasonId);
	Page<DivisionResponseDTO> fetchAllDivisions(int page, Boolean isActive);
	DivisionResponseDTO fetchDivisionById(Long divisionId);
	Page<MatchDayResponseDTO> fetchAllMatchDays(int page, Boolean isActive);
	MatchDayResponseDTO fetchMatchDayById(long matchDayId);
	Page<MatchResponseDTO> fetchAllMatches(int page, Boolean isActive);
	MatchResponseDTO fetchMatchById(Long matchId);
	Page<TeamResponseDTO> fetchAllTeams(int page, Boolean isActive);
	TeamResponseDTO fetchTeamById(Long teamId);
	
	Page<DivisionResponseDTO> fetchDivisionsBySeason(Long seasonId, int page, Boolean isActive);
	DivisionResponseDTO fetchDivisionBySeasonAndId(Long seasonId, Long divisionId);
	Page<MatchResponseDTO> fetchMatchesBySeason(Long seasonId, int page, Boolean isActive);
	Page<TeamResponseDTO> fetchTeamsBySeasonAndDivision (Long seasonId, Long divisionId, int page, Boolean isActive);
	Page<TeamResponseDTO> fetchTeamsBySeason(Long seasonId, int page, Boolean isActive);
	Page<TeamResponseDTO> fetchAllTeams(Pageable pageable, Boolean active, String name);
//	void fetchMatchesOfSeasonByMatchDay();
//	void fetchMatchDaysByDivision();
//	void fetchMatchDaysByDivisionAndId();
//	void fetchMatchByDivisionAndId();
//	void fetchMatchesByDivision();
//	void fetchMatchesByDivisionAndSeason();
//	void fetchTeamsByDivision();
//	void fetchTeamsByDivisionAndSeason();
//	void finishMatchesSeasonByMatchDay();
	
	
	void deleteSeason(Long id);
	void deleteDivision(Long id);
	void deleteMatchDay(Long id);
	void deleteMatch(Long id);
	void deleteTeam(Long id);
	
	SeasonResponseDTO updateSeason(Long id, SeasonUpdateRequestDTO dto);
	DivisionResponseDTO updateDivision(Long id, DivisionUpdateRequestDTO dto);
	MatchDayResponseDTO updateMatchDay(Long id, MatchDayRequestDTO dto);
	MatchResponseDTO updateMatch(Long id, MatchUpdateRequestDTO dto);
	TeamResponseDTO updateTeam(Long id, TeamRequestDTO dto,MultipartFile logoFile);
	
//	SeasonResponseDTO removeDivisionFromSeason(Long divisionId, Long seasonId);
	MatchDayResponseDTO removeMatchFromMatchDay(Long matchId, Long matchDayId);
	DivisionResponseDTO removeTeamFromDivision(Long teamId, Long divisionId);
	DivisionResponseDTO removeMatchDayFromDivision(Long matchId, Long divisionId);
//	MatchResponseDTO removeTeamFromMatch(Long matchId, Long teamId);
	
	SeasonResponseDTO createSeason(SeasonRequestDTO dto);
	DivisionResponseDTO createDivision(DivisionRequestDTO dto);
	MatchResponseDTO createMatch(MatchRequestDTO dto);
	TeamResponseDTO createTeam(TeamRequestDTO dto, MultipartFile file);
	MatchDayResponseDTO createMatchDay(MatchDayRequestDTO dto);
	
	MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto);
	DivisionResponseDTO addMatchDayToDivision(MatchDayAddToDivisionRequestDTO dto);
	SeasonResponseDTO addDivisionToSeason(DivisionAddToSeasonRequestDTO dto);
	DivisionResponseDTO addTeamToDivision(TeamAddToDivisionRequestDTO dto);
//	void addTeamToMatch();
	
}
