package rugbyniela.entity.dto.match;

import java.time.LocalDateTime;

import rugbyniela.entity.dto.address.AddressRequestDTO;

//TODO: possible problem with teams id and mapstruct
//TODO: we care only about divison's id
public record MatchRequestDTO(
		AddressRequestDTO location,
		String name,
		LocalDateTime timeMatchStart,
		Long localTeam,
		Long awayTeam) {

}
