package rugbyniela.entity.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import rugbyniela.entity.dto.address.AddressRequestDTO;

public record UserUpdatedRequestDTO(
		@NotBlank
		@Size(max=50)
		String name,
		@Size(max = 50)
		String surname,
		@Size(max=50)
		String nickname,
		int age, 
		@Size(max=50)
		String phoneNumber,
		@Size(max=80)
		String instagram,
		String gender,
		AddressRequestDTO address,
		boolean deletePicture) {

}
