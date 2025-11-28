package rugbyniela.entity.dto;

import java.util.Set;

public record FlatUserSeasonScoreDTO(
		Long id, 
		FlatUserDTO user,
		Set<UserMatchDayScoreDTO> ScorePerMatches
		) {

}
