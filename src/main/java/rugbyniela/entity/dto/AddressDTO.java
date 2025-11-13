package rugbyniela.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddressDTO {
	public Long id;
	public String street;
	public String city;
	public String postalCode;
	public String descripcion;
}

