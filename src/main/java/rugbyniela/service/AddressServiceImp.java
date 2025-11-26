package rugbyniela.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rugbyniela.entity.dto.AddressDTO;
import rugbyniela.entity.dto.AddressDTO;
import rugbyniela.entity.pojo.Address;
import rugbyniela.repository.AddressRepository;

@Service
public class AddressServiceImp implements IAddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public boolean createAddress(AddressDTO addressDTO) {
		if(addressDTO != null) {
			Address address = new Address(null,addressDTO.street(), addressDTO.city(), addressDTO.postalCode(),addressDTO.descripcion());
			addressRepository.save(address);			
			//insert into address(...) values(...)
			return true;
		}else {
			//TODO: error, DTO is null
			return false;
		}
	}

	@Override
	public AddressDTO findAddressById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Address> findAll() {
		//return null;
		return addressRepository.findAll();
	}

}
