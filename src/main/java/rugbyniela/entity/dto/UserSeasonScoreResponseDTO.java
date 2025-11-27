package rugbyniela.entity.dto;

import java.util.Set;

import rugbyniela.entity.pojo.Coalition;
import rugbyniela.entity.pojo.Season;
import rugbyniela.entity.pojo.User;
import rugbyniela.entity.pojo.UserMatchDayScore;

public record UserSeasonScoreResponseDTO (
		Long id, 
		int totalPoints,
		Set<Long> matchDayScoresId, 
		Long seasonId, 
		Long coalition,
		Long userId) //no 
{

}
