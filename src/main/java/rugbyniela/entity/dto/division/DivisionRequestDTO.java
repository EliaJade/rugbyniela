package rugbyniela.entity.dto.division;

import java.util.Set;

import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;

public record DivisionRequestDTO(
		String name,
		String category,
		Set<MatchDayRequestDTO> matchDays,
		Set<Long> teams) {

}
