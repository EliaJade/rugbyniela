package rugbyniela.entity.dto.userSeasonScore;

import rugbyniela.entity.dto.season.SeasonRequestDTO;

public record UserSeasonScoreRequestDTO(
		Long seasonId,
		Long userId
//		Long coalitionId
		) {

}
