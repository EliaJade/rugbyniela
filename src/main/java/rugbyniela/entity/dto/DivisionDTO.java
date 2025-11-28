package rugbyniela.entity.dto;

import java.util.Locale.Category;

public record DivisionDTO(
		Long id,
		String name,
		Category category,
		FlatMatchDayDTO dates //maybe better just use MatchDayDTO
		) {

}
