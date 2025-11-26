package rugbyniela.entity.dto;

public record UserRequestDTO(
		String name,
		String surname,
		int age,
		String phoneNumber,
		String email,
		String password,
		AddressDTO address) {

}
