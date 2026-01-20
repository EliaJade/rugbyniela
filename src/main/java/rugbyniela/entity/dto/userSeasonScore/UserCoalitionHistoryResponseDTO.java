package rugbyniela.entity.dto.userSeasonScore;

public record UserCoalitionHistoryResponseDTO(
		String coalitionName,
	    String seasonName,  
	    int score,
	    Long seasonId) {

}
