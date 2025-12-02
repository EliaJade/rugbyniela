package rugbyniela.entity.dto;

import java.util.Set;

public record UserSeasonScoreResponseDTO (
		Long id, 
		int totalPoints,
		Set<WeeklyBetTicketResponseDTO> bettingTickets,
		Set<UserMatchDayScoreDTO> ScorePerMatches,
		Long seasonId,
		Long userId,
		Long coalitionId
		)  
{

}
