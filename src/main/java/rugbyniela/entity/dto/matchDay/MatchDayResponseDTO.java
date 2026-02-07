package rugbyniela.entity.dto.matchDay;

import java.time.LocalDate;
import java.util.Set;

import rugbyniela.entity.dto.match.MatchResponseDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;

public record MatchDayResponseDTO(
		Long id,
		String name,
		Long divisionId,
		LocalDate dateBegin,
		LocalDate dateEnd,
		Set<MatchResponseDTO> matches,
		boolean isActive,
		boolean arePointsCalculated) {

}
