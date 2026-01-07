package rugbyniela.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.UserSeasonScoreRepository;

@Service
@Slf4j
public class BettingServiceImp implements BettingService{
	@Autowired
	private UserSeasonScoreRepository userSeasonScoreRepository;
	@Autowired
	private MatchDayRepository matchDayRepository;
	@Override
	public void submitTicket(WeeklyBetTicketRequestDTO dto) {
		// 1. Validate UserSeasonScore exists
		UserSeasonScore userSeason = userSeasonScoreRepository.findById(dto.userSeasonId())
				.orElseThrow(() -> new RugbyException("No se ha encontrado este usuario", HttpStatus.BAD_REQUEST, ActionType.BETTING));
//		if(userSeasonScoreRepository.findById(dto.userSeasonId())==null) {
//			log.warn("Intento fallido de entregar ticket. El usuario no existe",dto.userSeasonId());
//			throw new RugbyException("Este email ya existe", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);  
//		}
				
		// 2. Validate MatchDay exists and fetch matches
		MatchDay matchDay = matchDayRepository.findById(dto.matchDayId())
				.orElseThrow(() -> new RugbyException("No se ha encontrado la jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING));
		// 3. Validate user has not already submitted a ticket for this match day
		// 4. Validate that bets cover all matches in matchDay
		// 5. Create WeeklyBetTicket
		// 6. Create Bet entities from the DTO
		// 7. Save the ticket (cascade saves bets)
		// 8. Return some response DTO (you can create a mapper for this)
	}

	@Override
	public void cancelTicket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchUserSeasonTickets() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchUserSeasonTicketsByMatchDay() {
		// TODO Auto-generated method stub
		
	}

}
