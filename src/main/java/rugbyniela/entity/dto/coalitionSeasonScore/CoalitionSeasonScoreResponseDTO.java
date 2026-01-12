package rugbyniela.entity.dto.coalitionSeasonScore;

import java.util.List;

public record CoalitionSeasonScoreResponseDTO(
		Long id,
		int totalPoints,
		Long seasonId,
		Long coalitionId,
		List<CoalitionMatchDayScoreResponseDTO> coalitionMatchDays
		) {

}
