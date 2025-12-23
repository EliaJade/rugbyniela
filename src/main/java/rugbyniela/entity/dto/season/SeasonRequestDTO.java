package rugbyniela.entity.dto.season;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.division.DivisionRequestDTO;

public record SeasonRequestDTO(
		@NotBlank
		@Size(max=100)
		String name,
		LocalDate startSeason,
		LocalDate endSeason,
		Set<DivisionRequestDTO> divisions) {

}
