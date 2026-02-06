package rugbyniela.entity.dto.teamDivisionScore;

public record TeamDivisionScoreResponseDTO(
		Long id,
		Long seasonId,
		Long divisionId,
		Long teamId,
		String name,
		String teamPictureUrl,
		int totalPoints
		) {

}
