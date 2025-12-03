package rugbyniela.entity.dto;

import java.time.LocalDateTime;

import rugbyniela.entity.pojo.Bonus;

public record MatchResponseDTO(
		Long id,
		AddressDTO location,
		LocalDateTime timeMatchStart,
		Integer localResult,
		Integer awayResult,
		Bonus bonus,
		TeamDTO awayTeam,
		TeamDTO localTeam,
		Long matchDayId
		) {

}
