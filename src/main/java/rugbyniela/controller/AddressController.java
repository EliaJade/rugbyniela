package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rugbyniela.service.IAddressService;

//https://.../address/save

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private IAddressService  addressService;
	
	@PostMapping("/save")
	public ResponseEntity<?> save(){
		if(addressService.createAddress(null)) {
			return new ResponseEntity<>(HttpStatus.CREATED);
		}else {
		    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
