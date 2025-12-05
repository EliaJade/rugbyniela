package rugbyniela.entity.dto.season;

import java.time.LocalDate;

public record SeasonResponseDTO(
		Long id,
		String name,
		LocalDate startSeason,
		LocalDate endSeason) {

}
