package rugbyniela.entity.dto;

import java.util.Locale.Category;
import java.util.Set;

public record DivisionDTO(
		Long id,
		String name,
		Category category,
		Long seasonId,
		Set<MatchDayResponseDTO> matchDays 
		) {

}
