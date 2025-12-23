package rugbyniela.entity.dto.team;

import jakarta.validation.constraints.Size;

//for both add and update
public record TeamRequestDTO(
		@Size(max=50)
		String name,
		@Size(max=300)
		String url) {

}
