package rugbyniela.entity.dto;

import java.util.Set;

public record CoalitionDTO(
		Long id,
		String name,
		Set<FlatUserSeasonScoreDTO> users
		
		) {

}
