package rugbyniela.entity.dto.bet;

public record BetResponseDTO(
		Long id,
		int pointsAwared,
		Long weeklyBetTicketId,
		Long predictedWinnerId,
		String bonus,
		Long matchId) {

}
