package rugbyniela.entity.dto.division;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//TODO: here we'll allow add and remove matchDays
//TODO: create method to add and remove team
public record DivisionUpdateRequestDTO(
		@NotBlank
		@Size(max=50, min = 1)
		String name,
		@NotNull
		String category) {

}
