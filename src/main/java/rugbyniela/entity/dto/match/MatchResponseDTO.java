package rugbyniela.entity.dto.match;

import java.time.LocalDateTime;

import rugbyniela.entity.dto.address.AddressResponseDTO;

public record MatchResponseDTO(
		Long id,
		AddressResponseDTO location,
		LocalDateTime timeMatchStart,
		Integer localResult,
		Integer awayResult,
		String bonus,
		String matchStatus,
		String localTeamName,
		String awayTeamName) {

}
