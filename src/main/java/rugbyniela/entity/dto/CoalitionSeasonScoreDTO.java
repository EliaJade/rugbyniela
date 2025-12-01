package rugbyniela.entity.dto;

import java.util.Set;

public record CoalitionSeasonScoreDTO(
		Long id,
		int totalPoints,
		Long seasonId,
		Long coalitionId,
		Set<CoalitionMatchDayScoreDTO> coalitionMatchDays
		
		) {

	
}
