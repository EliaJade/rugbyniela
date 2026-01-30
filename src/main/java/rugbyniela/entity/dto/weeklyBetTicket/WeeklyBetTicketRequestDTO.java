package rugbyniela.entity.dto.weeklyBetTicket;

import java.util.Set;

import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;

public record WeeklyBetTicketRequestDTO(
		//TODO: the creationDate will be assigning in the service
		Long userSeasonId,
		Long matchDayId,
		Set<BetRequestDTO> bets,
		Set<DivisionBetDTO> divisionBets
//		Long predictedLeaderboardWinner
		){

	

}
