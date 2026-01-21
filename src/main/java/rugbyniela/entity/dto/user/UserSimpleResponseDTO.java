package rugbyniela.entity.dto.user;

//DTO to use as leader in coalition
public record UserSimpleResponseDTO(
		Long id,
		String name,
		String nickname
		//TODO: add to which coalition is part of
		) {

}
