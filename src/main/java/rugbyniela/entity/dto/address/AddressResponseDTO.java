package rugbyniela.entity.dto.address;


public record AddressResponseDTO(
		Long id,
		String street,
		String city,
		String postalCode,
		String description) {

}
