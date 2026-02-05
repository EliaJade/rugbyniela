package rugbyniela.security;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import rugbyniela.entity.pojo.Token;
import rugbyniela.entity.pojo.User;
import rugbyniela.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler{

	private final TokenRepository tokenRepository;
	

	@Override
	public void logout(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			//TODO: add log or something
			return;
		}
		jwt = authHeader.substring(7);
		
		Token storedToken = tokenRepository.findByToken(jwt).orElse(null);
		
		if(storedToken != null) {
			User user = storedToken.getUser();
			List<Token> allUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
			if(allUserTokens.isEmpty()) {
				return;
			}
			allUserTokens.forEach(token->{
				token.setExpired(true);
				token.setRevoked(true);
			});
			tokenRepository.saveAll(allUserTokens);
            SecurityContextHolder.clearContext();
		}
	}
}
