package rugbyniela.entity.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//for both add and update
public record TeamRequestDTO(
		@NotBlank(message = "El equipo deber tener nombre")
		@Size(max=50, message= "El nombre no puede superar los 50 caracteres")
		String name,
		@Size(max=300, message= "El url no puede superar los 300 caracteres")
		String url) {

}
