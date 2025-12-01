package rugbyniela.entity.dto;

import java.util.Set;

public record CoalitionDTO(
		Long id,
		String name,
		Set<UserSeasonScoreResponseDTO> users,
		Set<CoalitionSeasonScoreDTO> coalitionSeasonScores
		
		) {

}
