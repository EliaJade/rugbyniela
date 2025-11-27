package rugbyniela.entity.dto;

public record UserResponseDTO(
		Long id,
		String name,
		String surname,
		String nickname,
		int age,
		String phoneNumber,
		String email,
		Long addressId) //no password
{
	
}
