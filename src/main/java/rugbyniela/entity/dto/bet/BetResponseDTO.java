package rugbyniela.entity.dto.bet;

import rugbyniela.enums.BetResult;

public record BetResponseDTO(
		Long id,
		int pointsAwarded,
		Long weeklyBetTicketId,
		Long predictedWinnerId,
		String bonus,
		String betResult,
		Long matchId) {

}
