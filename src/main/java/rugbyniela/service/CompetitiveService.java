package rugbyniela.service;

import org.springframework.data.domain.Page;

import rugbyniela.entity.pojo.Season;

public interface CompetitiveService {

	Page<Season> fetchAllSeasons(Long seasonId, int page);
	void fetchSeasonById();
	void fetchDivisionsBySeason();
	void fetchDivisionBySeasonAndId();
	void fetchAllTeams();
	void fetchTeamById();
	void createSeason();
	void createDivision();
	void createMatch();
	void createTeam();
	void addDivisionToSeason();
	void fetchMatchesOfSeason();
	void fetchMatchesOfSeasonByMatchDay();
	void finishMatchesSeasonByMatchDay();
	
}
