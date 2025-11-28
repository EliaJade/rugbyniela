package rugbyniela.entity.dto;

public record BetDTO(
		Long id,
		int pointsAwarded,
		MatchResponseDTO match,
		String predictedWinnerTeamName) {

}
