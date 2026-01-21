package rugbyniela.entity.dto.coalition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CoalitionRequestDTO(
		@NotBlank(message = "El nombre es obligatorio")
	    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
		String name) {

}
