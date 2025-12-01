package rugbyniela.entity.dto;

import java.time.LocalDate;
import java.util.Set;

public record MatchDayResponseDTO(
		Long id,
		LocalDate dateBegin,
		LocalDate dateEnd,
		String name,
		Long divisionId,
		Set<MatchResponseDTO> matches
		) {

}
