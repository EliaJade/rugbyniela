package rugbyniela.entity.dto.season;

import java.time.LocalDate;

public record SeasonUpdateRequestDTO(
		String name,
		LocalDate startSeason,
		LocalDate endSeason) {

}
