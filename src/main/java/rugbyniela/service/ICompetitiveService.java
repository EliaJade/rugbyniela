package rugbyniela.service;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.domain.Page;

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
import rugbyniela.entity.dto.team.TeamAddToDivisionRequestDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;

public interface ICompetitiveService {

	Page<SeasonResponseDTO> fetchAllSeasons(int page);
	SeasonResponseDTO fetchSeasonById(Long seasonId);
	Page<DivisionResponseDTO> fetchAllDivisions(int page);
	DivisionResponseDTO fetchDivisionById(Long divisionId);
	Page<MatchDayResponseDTO> fetchAllMatchDays(int page);
	MatchDayResponseDTO fetchMatchDayById(long matchDayId);
	Page<MatchResponseDTO> fetchAllMatches(int page);
	MatchResponseDTO fetchMatchById(Long matchId);
	Page<TeamResponseDTO> fetchAllTeams(int page);
	TeamResponseDTO fetchTeamById(Long teamId);
	
	Page<DivisionResponseDTO> fetchDivisionsBySeason(Long seasonId, int page);
	DivisionResponseDTO fetchDivisionBySeasonAndId(Long seasonId, Long divisionId);
	Page<MatchResponseDTO> fetchMatchesBySeason(Long seasonId, int page);
//	void fetchMatchesOfSeasonByMatchDay();
//	void fetchMatchDaysByDivision();
//	void fetchMatchDaysByDivisionAndId();
//	void fetchMatchByDivisionAndId();
//	void fetchMatchesByDivision();
//	void fetchMatchesByDivisionAndSeason();
//	void fetchTeamsByDivision();
//	void fetchTeamsByDivisionAndSeason();
//	
//	void finishMatchesSeasonByMatchDay();
	
	SeasonResponseDTO createSeason(SeasonRequestDTO dto);
	DivisionResponseDTO createDivision(DivisionRequestDTO dto);
	MatchResponseDTO createMatch(MatchRequestDTO dto);
	TeamResponseDTO createTeam(TeamRequestDTO dto);
	MatchDayResponseDTO createMatchDay(MatchDayRequestDTO dto);
	
	MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto);
	DivisionResponseDTO addMatchDayToDivision(MatchDayAddToDivisionRequestDTO dto);
	SeasonResponseDTO addDivisionToSeason(DivisionAddToSeasonRequestDTO dto);
	DivisionResponseDTO addTeamToDivision(TeamAddToDivisionRequestDTO dto);
	
	
}
