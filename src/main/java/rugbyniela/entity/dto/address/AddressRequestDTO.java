package rugbyniela.entity.dto.address;

//for both add and update
public record AddressRequestDTO(
		String street,
		String city,
		String postalCode,
		String description) {

}
