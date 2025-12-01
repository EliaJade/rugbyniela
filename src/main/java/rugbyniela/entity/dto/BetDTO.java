package rugbyniela.entity.dto;

public record BetDTO(
		Long id,
		int pointsAwarded,
		Long weeklyBetTicketId,
		MatchResponseDTO match,
		Long predictedWinnerTeamId) {

}
