package rugbyniela.entity.dto.matchDay;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//TODO: for both add and update
public record MatchDayRequestDTO(
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate dateBegin,
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate dateEnd,
		Long divisionId,
		@NotBlank(message = "La jornada debe tener un nombre")
		@Size(max=100, message = "El nombre de la jornada no puede tener mas de 100 caracteres")
		String name) {
}
