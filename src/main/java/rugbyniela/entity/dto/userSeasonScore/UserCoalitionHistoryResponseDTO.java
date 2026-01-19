package rugbyniela.entity.dto.userSeasonScore;

public record UserCoalitionHistoryResponseDTO(
		String coalitionName,
	    String seasonName,  
	    Double score,
	    Long seasonId) {

}
