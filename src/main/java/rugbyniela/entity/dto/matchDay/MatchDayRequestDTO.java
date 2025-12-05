package rugbyniela.entity.dto.matchDay;

import java.time.LocalDate;

//TODO: for both add and update
public record MatchDayRequestDTO(
		LocalDate dateBegin,
		LocalDate dateEnd,
		String name) {

}
