package rugbyniela.entity.dto.division;

import java.util.Set;

import rugbyniela.entity.dto.matchDay.MatchDayResponseDTO;
import rugbyniela.entity.dto.team.TeamResponseDTO;

public record DivisionResponseDTO(
		Long id,
		String name,
		String category,
		Long seasonId,
		Set<MatchDayResponseDTO> matchDays,
		Set<TeamResponseDTO> teams) {
}
