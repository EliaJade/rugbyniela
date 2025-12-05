package rugbyniela.entity.dto.season;

import java.time.LocalDate;
import java.util.Set;

import rugbyniela.entity.dto.division.DivisionRequestDTO;

public record SeasonRequestDTO(
		String name,
		LocalDate startSeason,
		LocalDate endSeason,
		Set<DivisionRequestDTO> divisions) {

}
