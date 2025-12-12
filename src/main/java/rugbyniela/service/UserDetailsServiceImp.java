package rugbyniela.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import rugbyniela.entity.pojo.User;
import rugbyniela.repository.UserRepository;
import rugbyniela.security.SecurityUser;

@Service
@AllArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService{

	private final UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//TODO: change the exception to a personalize one
		User user = repository.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User with the email "+email+" doesn't exist in the database"));
		return new SecurityUser(user);
	}

}
