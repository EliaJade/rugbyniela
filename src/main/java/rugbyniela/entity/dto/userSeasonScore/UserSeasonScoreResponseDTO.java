package rugbyniela.entity.dto.userSeasonScore;

public record UserSeasonScoreResponseDTO(
		Long id,
		int totalPoints,
		Long seasonId,
		Long userId,
		Long coalitionId,
		String coalitionName){}
