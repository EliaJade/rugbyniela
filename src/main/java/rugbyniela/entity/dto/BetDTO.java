package rugbyniela.entity.dto;

public record BetDTO(
		Long id,
		int pointsAwarded,
		Long matchId,
		Long predictedWinnerId) {

}
