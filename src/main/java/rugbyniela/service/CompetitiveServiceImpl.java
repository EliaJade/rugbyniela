package rugbyniela.service;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import rugbyniela.entity.pojo.Season;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.SeasonRepository;

public class CompetitiveServiceImpl implements CompetitiveService{

//	private final SeasonRepository seasonRepository;
	@Override
	public Page<Season> fetchAllSeasons(Long seasonId, int page) {
//		//Validate there are seasons
//		Season season = seasonRepository.findById(seasonId);
//		if(page<0) {
//			throw new RugbyException("No hay temporadas", HttpStatus.BAD_REQUEST, ActionType.BETTING);
//		}
//		Pageable pageable = PageRequest.of(page, 10, Sort.by("creationDate").descending());
//		
		return null;
		
		
	}

	@Override
	public void fetchSeasonById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchDivisionsBySeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchDivisionBySeasonAndId() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchAllTeams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchTeamById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createSeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDivision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTeam() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDivisionToSeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchMatchesOfSeason() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchMatchesOfSeasonByMatchDay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishMatchesSeasonByMatchDay() {
		// TODO Auto-generated method stub
		
	}
	
	

}
