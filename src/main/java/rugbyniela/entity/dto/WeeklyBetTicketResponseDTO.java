package rugbyniela.entity.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record WeeklyBetTicketResponseDTO(
		Long id,
		LocalDateTime creationDate,
		Long userSeasonId,
		Set<BetDTO> bets) {

}
