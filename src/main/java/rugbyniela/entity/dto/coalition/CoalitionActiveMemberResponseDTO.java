package rugbyniela.entity.dto.coalition;

public record CoalitionActiveMemberResponseDTO(
		Long userId,
	    String nickname,
	    String role,          // CAPITAN, USER...
	    String profilePicture,
	    // Datos de temporada (pueden ser nulos o 0 si no está inscrito)
	    Integer seasonPoints, 
	    boolean isRegisteredInSeason) {

}
