package rugbyniela.entity.dto.user;

import rugbyniela.entity.dto.address.AddressRequestDTO;

public record UserRequestDTO(

		String name,
		String surname,
		String nickname,
		int age, 
		String phoneNumber,
		String email,
		String password,
		String instagram,
		String gender,
		AddressRequestDTO address) {

}
