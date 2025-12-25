package rugbyniela.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import rugbyniela.enums.TokenType;
import rugbyniela.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class TokenValidator {

	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	
	public boolean isValid(String token, UserDetails user, TokenType type) {
		if(!jwtService.isTokenValid(token, user)) {
			return false;
		}
		return tokenRepository.findByToken(token)
				.map(t -> !t.isExpired() && !t.isRevoked() && t.getTokenType() == type)
				.orElse(false);
	}
}
