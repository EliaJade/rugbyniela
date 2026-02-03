package rugbyniela.entity.dto.coalition;

public record CoalitionSimpleResponseDTO(
		Long id,
	    String name,
	    String captainName,
	    int membersCount,
	    boolean active) {

}
