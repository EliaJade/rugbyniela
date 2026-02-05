package rugbyniela.entity.dto.team;

public record TeamResponseDTO(
		Long id,
		String name,
		String url,
		String teamPictureUrl,
		boolean isActive) {

}
