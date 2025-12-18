package rugbyniela.entity.dto.weeklyBetTicket;

import java.util.Set;

import rugbyniela.entity.dto.bet.BetRequestDTO;
import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;


public record WeeklyBetTicketUpdateRequestDTO(
		//TODO: creation time will be updated in the service
		Set<BetRequestDTO> bets,
		Set<DivisionBetDTO> divisionBets) {
}
