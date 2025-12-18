package rugbyniela.entity.dto.collaborator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//for both add and update
public record CollaboratorRequestDTO(
		@Size(max = 80)
		@NotBlank
		String name,
		@Size(max = 300)
		String url,
		@Size(max = 500)
		String pictureUrl) {

}
