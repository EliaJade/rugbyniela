package rugbyniela.entity.dto.collaborator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//for both add and update
public record CollaboratorRequestDTO(
		@Size(max = 80, message = "El nombre del colaborador debe tener menos de 80 caracteres")
		@NotBlank(message = "El colaborador debe tener un nombre")
		String name,
		@Size(max = 500, message = "El url del colaborador debe tener menos de 500 caracteres")
		String url) {

}
