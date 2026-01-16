package rugbyniela.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String SECRET_KEY;
	private final long TOKEN_EXPIRATION;
	private final long REFRESH_EXPIRATION;
	
	public JwtService(
			@Value("${application.security.jwt.secret-key}") String SECRET_KEY,
			@Value("${application.security.jwt.expiration.token}") long TOKEN_EXPIRATION,
			@Value("${application.security.jwt.refresh.token}") long REFRESH_EXPIRATION) {
		this.SECRET_KEY=SECRET_KEY;
		this.TOKEN_EXPIRATION = TOKEN_EXPIRATION;
		this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(),userDetails,TOKEN_EXPIRATION);
	}
	
	public String generateRefreshToken(UserDetails userDetails){
		return generateToken(new HashMap<>(), userDetails,REFRESH_EXPIRATION);
	}
	
	private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // El email es el subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Expira en 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
