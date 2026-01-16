package rugbyniela.entity.dto.coalition;

import jakarta.validation.constraints.NotNull;

public record CoalitionJoinRequestDTO(
		@NotNull(message = "El ID de la coalición es obligatorio")
		Long coalitionId) {

}
