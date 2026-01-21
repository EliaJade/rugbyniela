package rugbyniela.mapper;

import org.mapstruct.Mapper;

import rugbyniela.entity.dto.address.AddressRequestDTO;
import rugbyniela.entity.dto.address.AddressResponseDTO;
import rugbyniela.entity.pojo.Address;

@Mapper(componentModel = "spring")
public interface IAddressMapper {

	Address toEntity(AddressRequestDTO dto);
	AddressResponseDTO toDTO(Address address);
}
