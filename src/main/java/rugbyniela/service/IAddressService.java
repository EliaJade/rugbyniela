package rugbyniela.service;

import java.util.List;

import rugbyniela.entity.dto.AddressDTO;
import rugbyniela.entity.pojo.Address;

public interface IAddressService {

	public boolean createAddress(AddressDTO addressDTO);
	public AddressDTO findAddressById(Long id);
	public List<Address> findAll();
}
