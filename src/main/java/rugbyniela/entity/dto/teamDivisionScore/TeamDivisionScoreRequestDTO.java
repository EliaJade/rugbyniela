package rugbyniela.entity.dto.teamDivisionScore;

public record TeamDivisionScoreRequestDTO (
		Long seasonId,
		Long divisionId,
		Long teamId,
		int puntosTotales
		) {

}
