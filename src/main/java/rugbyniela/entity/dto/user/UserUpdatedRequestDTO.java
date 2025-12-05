package rugbyniela.entity.dto.user;

public record UserUpdatedRequestDTO(
		String name,
		String surname,
		String nickname,
		int age, 
		String phoneNumber,
		String instagram,
		String gender) {

}
