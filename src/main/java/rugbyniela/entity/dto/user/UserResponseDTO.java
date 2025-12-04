package rugbyniela.entity.dto.user;

import rugbyniela.entity.dto.address.AddressResponseDTO;

public record UserResponseDTO(
		Long id,
		String name,
		String surname,
		String nickname,
		int age, 
		String phoneNumber,
		String email,
		String instagram,
		String gender,
		AddressResponseDTO addres) {

}
