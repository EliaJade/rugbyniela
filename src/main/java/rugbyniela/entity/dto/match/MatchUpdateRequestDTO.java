package rugbyniela.entity.dto.match;

import java.time.LocalDateTime;

import rugbyniela.entity.dto.address.AddressRequestDTO;

//TODO: we care only about match's id
public record MatchUpdateRequestDTO(
		AddressRequestDTO location,
		String name,
		LocalDateTime timeMatchStart,
		Long localTeam,
		Long awayTeam,
		Integer localResult,
		Integer awayResult,
		String bonus,
		String status) {

}
