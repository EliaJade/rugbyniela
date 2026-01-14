package rugbyniela.entity.dto.coalition;

import java.time.LocalDateTime;

public record CoalitionJoinResponseDTO(
		Long id,
		Long coalitionId,
		String name,
		LocalDateTime requestedAt) {

}
