package rugbyniela.entity.dto;

import java.time.LocalDate;
import java.util.Set;

import rugbyniela.entity.pojo.CoalitionSeasonScore;
import rugbyniela.entity.pojo.Division;
import rugbyniela.entity.pojo.UserSeasonScore;

public record SeasonDTO(
		Long id,
		String name,
		LocalDate startSeason,
		LocalDate endSeason,
		Set<UserSeasonScoreResponseDTO> seasonParticipants,
		Set<CoalitionSeasonScoreDTO> coalSeasonScores,
		Set<DivisionDTO> divisions
		) {

}
