package rugbyniela.entity.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//for both add and update
public record AddressRequestDTO(
		@Size( max = 100)
		String street,
		@NotBlank
		@Size( max = 50)
		String city,
		@NotBlank
		@Size( max = 30)
		String postalCode,
		@Size( max = 100)
		String description) {

}
