package rugbyniela.entity.dto.weeklyBetTicket;

import java.time.LocalDateTime;
import java.util.Set;

import rugbyniela.entity.dto.bet.BetResponseDTO;
import rugbyniela.entity.dto.divisionBet.DivisionBetDTO;

public record WeeklyBetTicketResponseDTO(
		Long id,
		Long userSeasonId,
		LocalDateTime creationDate,
		Set<BetResponseDTO> bets,
		Set<DivisionBetDTO> divisionBets
//		Long predictedLeaderboardWinner
		){

}
