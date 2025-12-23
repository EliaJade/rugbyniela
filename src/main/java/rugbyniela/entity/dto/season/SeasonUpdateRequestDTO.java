package rugbyniela.entity.dto.season;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SeasonUpdateRequestDTO(
		@NotBlank
		@Size(max=100)
		String name,
		LocalDate startSeason,
		LocalDate endSeason) {

}
