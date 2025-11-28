package rugbyniela.entity.dto;

import java.util.Set;

public record UserResponseDTO(
		Long id,
		String name,
		String surname,
		String nickname,
		int age,
		String phoneNumber,
		String email,
		AddressDTO address,
		Set<UserMatchDayScoreDTO> userSeasonScore) //no password
{
	
}
