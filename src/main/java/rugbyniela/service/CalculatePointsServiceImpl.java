package rugbyniela.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.bet.BetResponseDTO;
import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.pojo.Bet;
import rugbyniela.entity.pojo.Match;
import rugbyniela.entity.pojo.MatchDay;
import rugbyniela.entity.pojo.Team;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.WeeklyBetTicket;
import rugbyniela.enums.ActionType;
import rugbyniela.enums.Bonus;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.BetRepository;
import rugbyniela.repository.WeeklyBetTicketRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatePointsServiceImpl implements ICalculatePointsService{

	private BetRepository betRepository;
	private WeeklyBetTicketRepository weeklyBetTicketRepository;
	
	@Override
	public void calculatePointsByBet(Long betId) {
		Bet bet = checkBet(betId);
		Match match = bet.getMatch();
		MatchDay matchDay = match.getMatchDay();
		WeeklyBetTicket ticket = bet.getWeeklyBetTicket();
		int weeklyPoints =0;
		if(match.getLocalResult() == match.getAwayResult()) {
			bet.setBetCorrect(bet.getPredictedWinner() == null ? true : false);
		}
		else {
		Team winner = match.getLocalResult() > match.getAwayResult()
		        ? match.getLocalTeam()
		        : match.getAwayTeam();
		
		if(bet.getPredictedWinner().equals(winner)) {
			bet.setBetCorrect(true);;
			if(bet.getBonus()!=(Bonus.NONE)) {
				if(bet.getBonus() == (match.getBonus())) {
//					bet.setPointsAwarded(bet.getPointsAwarded()+1);
					weeklyPoints=1;
				} 
				 else if (bet.getBonus()!=(match.getBonus())){
//					bet.setPointsAwarded(bet.getPointsAwarded()-1);
					weeklyPoints=-1;
				}
			}
			
		}
		else {
			bet.setBetCorrect(false);
		}
		}
		ticket.setWeeklyPoints(weeklyPoints);
		log.debug("PointsAwarded: "+bet.getPointsAwarded());
		log.debug("Was bet correct: " + bet.getBetCorrect());
	}

	@Override
	public int calculatePointsByWeeklyBetTicket(Long weeklyBetTicketId) {
		WeeklyBetTicket ticket = checkTicket(weeklyBetTicketId);
//		int counterCorrectBets = 0;
//		for (Bet bet : ticket.getBets()) {
//			if(bet.getBetCorrect()==true) {
//				counterCorrectBets= counterCorrectBets + 1;
//			}
//		}
		int betAmount = ticket.getBets().size();
		long correctBets = ticket.getBets().stream()
		        .filter(bet -> Boolean.TRUE.equals(bet.getBetCorrect()))
		        .count();
		log.debug("How many correctBets have there been" + correctBets);
//		if(correctBets ==1) {
//			ticket.setWeeklyPoints(ticket.getWeeklyPoints()+1);
//		}
//		if(correctBets ==2) {
//			ticket.setWeeklyPoints(ticket.getWeeklyPoints()+3);
//		}
//		if(correctBets ==3) {
//			ticket.setWeeklyPoints(ticket.getWeeklyPoints()+5);
//		}
//		if(correctBets ==4) {
//			ticket.setWeeklyPoints(ticket.getWeeklyPoints()+7);
//		}
		int points = switch ((int) correctBets) {
		    case 1 -> 1;
		    case 2 -> 3;
		    case 3 -> 5;
		    case 4 -> 7;
		    default -> 0;
		};
		ticket.setWeeklyPoints(ticket.getWeeklyPoints() + points);
		
		
//		if(ticket.getDivisionBets().stream().map(divisionBet->divisionBet.getPredictedLeader().equals(matchDay.get)))
		return 0;
	}

	@Override
	public int calculateTotalPoints() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Bet checkBet (Long id) {
		Bet bet = betRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Apuesta no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return bet;
		
	}
	
	public WeeklyBetTicket checkTicket (Long id) {
		WeeklyBetTicket ticket = weeklyBetTicketRepository.findById(id)
				.orElseThrow(()-> new RugbyException("Ticket de apuesta no encontrado", HttpStatus.NOT_FOUND, ActionType.BETTING));
		return ticket;
		
	}

}
