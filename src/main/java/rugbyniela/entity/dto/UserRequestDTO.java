package rugbyniela.entity.dto;

public record UserRequestDTO(
		String name,
		String surname,
		String nickname,
		int age,
		String phoneNumber,
		String email,
		String password,
		AddressDTO address) //no id, no is active
{

}
