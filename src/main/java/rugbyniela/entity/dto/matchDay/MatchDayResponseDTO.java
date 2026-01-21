package rugbyniela.entity.dto.matchDay;

import java.util.Set;

import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;

public record MatchDayResponseDTO(
		Long id,
		String name,
		Long divisionId,
		Set<MatchResponseDTO> matches,
		//we have to configure mapstruct to do this
		Set<TeamResponseDTO> teamDivision,
		boolean arePointsCalculated) {

}
