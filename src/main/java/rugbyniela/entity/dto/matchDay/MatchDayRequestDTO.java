package rugbyniela.entity.dto.matchDay;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//TODO: for both add and update
public record MatchDayRequestDTO(
		LocalDate dateBegin,
		LocalDate dateEnd,
		Long divisionId,
		@NotBlank(message = "La jornada debe tener un nombre")
		@Size(max=100, message = "El nombre de la jornada no puede tener mas de 100 caracteres")
		String name) {

}
