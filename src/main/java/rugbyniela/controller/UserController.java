package rugbyniela.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rugbyniela.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService iUserService;
	
//	@GetMapping("/find/{id}")
//	public ResponseEntity<?> fetchUserById(@PathVariable("id") Long id){
//		UserResponseDTO response = iUserService.fetchUserById(id);
//		
//		if(response==null) {
//			System.out.println("is null");
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}else {
//			System.out.println("was found");
//			return new ResponseEntity<>(response,HttpStatus.FOUND);
//		}
//	}
}
