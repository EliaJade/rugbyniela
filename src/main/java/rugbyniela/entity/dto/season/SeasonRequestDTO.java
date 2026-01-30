package rugbyniela.entity.dto.season;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.division.DivisionRequestDTO;

public record SeasonRequestDTO(
		@NotBlank(message = "Debe tener un nombre la temporada")
		@Size(max=100, message = "El nombre debe tener un maximo de 100 caracteres")
		String name,
		@NotNull(message = "La fecha de inicio es necestario")
		LocalDate startSeason,
		LocalDate endSeason,
		@NotNull(message = "es necesario tener divisiones")
		@Size(min = 1, message = "Debe haber al menos una division")
		Set<DivisionRequestDTO> divisions) {
}

