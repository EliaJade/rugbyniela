package rugbyniela.entity.dto.division;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;

public record DivisionRequestDTO(
		@NotBlank
		@Size(max=50, min = 1)
		String name,
		@NotNull
		String category,
		Set<MatchDayRequestDTO> matchDays,
		Set<Long> teams) {

}
