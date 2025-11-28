package rugbyniela.entity.dto;

import java.time.LocalDate;

public record FlatMatchDayDTO(
		Long id,
		LocalDate dateBegin,
		LocalDate dateEnd,
		String name
		) { //maybe useless

}
