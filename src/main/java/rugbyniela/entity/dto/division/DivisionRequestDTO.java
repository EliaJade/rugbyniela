package rugbyniela.entity.dto.division;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.matchDay.MatchDayRequestDTO;

public record DivisionRequestDTO(
		@NotBlank(message = "La division debe tener nombre")
		@Size(max=50, message = "El nombre de la division no puede superar los 50 caracteres")
		String name,
		@NotNull(message = "La division debe tener categoria")
		String category
		) {

}
