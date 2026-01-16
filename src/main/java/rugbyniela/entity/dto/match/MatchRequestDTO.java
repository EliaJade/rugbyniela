package rugbyniela.entity.dto.match;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.address.AddressRequestDTO;

//TODO: possible problem with teams id and mapstruct
//TODO: we care only about divison's id
public record MatchRequestDTO(
		@NotNull
		AddressRequestDTO location,
		@NotBlank
		@Size(max = 100)
		String name,
		@NotNull
		LocalDateTime timeMatchStart,
		@NotNull
		Long localTeam,
		@NotNull
		Long awayTeam) {

}
