package rugbyniela.entity.dto.coalition;

import java.util.Set;

import rugbyniela.entity.dto.coalitionSeasonScore.CoalitionSeasonScoreResponseDTO;
import rugbyniela.entity.dto.user.UserSimpleResponseDTO;
import rugbyniela.entity.dto.userSeasonScore.UserSeasonScoreResponseDTO;

public record CoalitionResponseDTO(
		Long id,
		String name,
		UserSimpleResponseDTO capitan,
		Set<CoalitionSeasonScoreResponseDTO> coalitionSeasonScores,
		Set<CoalitionActiveMemberResponseDTO> userSeasonScores
		//Set<UserSeasonScoreResponseDTO> userSeasonsScores
		) {

}
