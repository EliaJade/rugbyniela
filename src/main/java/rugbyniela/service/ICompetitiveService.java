package rugbyniela.service;

public interface ICompetitiveService {

	void fetchAllSeasons();
	void fetchSeasonById();
	void fetchDivisionsBySeason();
	void fetchDivisionBySeasonAndId();
	void fetchAllTeams();
	void fetchTeamById();
	void createSeason();
	void createDivision();
	void addDivisionToSeason();
	void fetchMatchesOfSeason();
	void fetchMatchesOfSeasonByMatchDay();
	void finishMatchesSeasonByMatchDay();
	
}
