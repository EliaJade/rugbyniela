package rugbyniela.entity.dto.match;

import rugbyniela.enums.Bonus;

public record MatchResultDTO(
		Long matchId,
		Long localTeamId,
		Long awayTeamId,
		Integer localResult,
		Integer awayResult,
		Bonus bonus
		) {

}
