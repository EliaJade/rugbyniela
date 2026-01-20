package rugbyniela.service;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.domain.Page;

import rugbyniela.entity.dto.division.DivisionRequestDTO;
import rugbyniela.entity.dto.division.DivisionResponseDTO;
import rugbyniela.entity.dto.match.MatchAddToMatchDayRequestDTO;
import rugbyniela.entity.dto.match.MatchRequestDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;
import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.season.SeasonRequestDTO;
import rugbyniela.entity.dto.season.SeasonResponseDTO;
import rugbyniela.entity.dto.team.TeamRequestDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.Team;

public interface CompetitiveService {

	Page<Season> fetchAllSeasons(int page);
	Season fetchSeasonById(Long seasonId);
	Page<Division> fetchDivisionsBySeason(Long seasonId, int page);
	Division fetchDivisionBySeasonAndId(Long seasonId, Long divisionId);
	Page<Team> fetchAllTeams(int page);
	Team fetchTeamById(Long teamId);
	SeasonResponseDTO createSeason(SeasonRequestDTO dto);
	DivisionResponseDTO createDivision(DivisionRequestDTO dto);
	MatchResponseDTO createMatch(MatchRequestDTO dto);
	TeamResponseDTO createTeam(TeamRequestDTO dto);
	MatchDayResponseDTO createMatchDay(MatchDayRequestDTO dto);
	MatchDayResponseDTO addMatchToMatchDay(MatchAddToMatchDayRequestDTO dto);
	void addDivisionToSeason();
	void fetchMatchesOfSeason();
	void fetchMatchesOfSeasonByMatchDay();
	void finishMatchesSeasonByMatchDay();
	
}
