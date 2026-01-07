package rugbyniela.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.weeklyBetTicket.WeeklyBetTicketRequestDTO;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.UserSeasonScore;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Bonus;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.MatchDayRepository;
import rugbyniela.repository.MatchRepository;
import rugbyniela.repository.TeamRepository;
import rugbyniela.repository.UserSeasonScoreRepository;
import rugbyniela.repository.WeeklyBetTicketRepository;

@Service
@Slf4j
public class BettingServiceImp implements BettingService{
	@Autowired
	private UserSeasonScoreRepository userSeasonScoreRepository;
	@Autowired
	private MatchDayRepository matchDayRepository;
	@Autowired
	private WeeklyBetTicketRepository weeklyBetTicketRepository;
	@Autowired
	private MatchRepository matchRepository;
	@Autowired
	private TeamRepository teamRepository;
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
		boolean ticketExists = weeklyBetTicketRepository.existsByUserSeasonAndMatchDay(userSeason, matchDay);
		if(ticketExists) {
			throw new RugbyException("Ya has enviado un ticket para este jornada", HttpStatus.BAD_REQUEST, ActionType.BETTING);
		} // TODO: change exception to update, aka if(ticketExists) { update(edit)} else { create new ticket
		
		// 4. Validate that bets cover all matches in matchDay
		Set<Match> matchesInMatchDay = matchDay.getMatches();
		if(dto.bets().size() != matchesInMatchDay.size()) {
			throw new RugbyException("Debe apostar a todos los partidos", HttpStatus.BAD_REQUEST, ActionType.BETTING);
		}
		
		
		// 5. Create WeeklyBetTicket
		WeeklyBetTicket ticket = new WeeklyBetTicket();
		ticket.setCreationDate(LocalDateTime.now());
		ticket.setUserSeason(userSeason);
		
		
		
		// 6. Create Bet entities from the DTO
		for(BetRequestDTO betDTO : dto.bets()) {
			Bet bet = new Bet();
			bet.setMatch(matchRepository.findById(betDTO.matchId()).orElseThrow(()-> new RugbyException("Partido no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
			bet.setPredictedWinner(teamRepository.findById(betDTO.predictedWinnerId()).orElseThrow(() -> new RugbyException("Equipo no encontrado", HttpStatus.BAD_REQUEST, ActionType.BETTING)));
			bet.setBonus(Bonus.valueOf(betDTO.bonus())); //TODO: what's the best way to deal with  try {
				//		    bet.setBonus(Bonus.valueOf(betDto.bonus().toUpperCase()));
				//		} catch (IllegalArgumentException e) {
				//		    throw new RugbyException("Bonus inválido", HttpStatus.BAD_REQUEST, ActionType.REGISTRATION);
				//		} or put in dtoBet the enum instead of string
			bet.setPointsAwarded(0);
			ticket.addBet(bet);
		}
		// 7. Save the ticket (cascade saves bets)
		weeklyBetTicketRepository.save(ticket);
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
