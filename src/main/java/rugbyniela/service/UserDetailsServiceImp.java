package rugbyniela.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import rugbyniela.entity.pojo.SecurityUser;
import rugbyniela.entity.pojo.User;
import rugbyniela.enums.ActionType;
import rugbyniela.exception.RugbyException;
import rugbyniela.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService{

	private final UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//TODO: change the exception to a personalize one
		User user = repository.findByEmail(email)
				.orElseThrow(()-> new RugbyException("El usuario con el email "+email+" no se encuenta en la base de datos",
						HttpStatus.NOT_FOUND,
						ActionType.AUTHENTICATION));
		return new SecurityUser(user);
	}

}
