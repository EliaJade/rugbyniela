package rugbyniela.security;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import rugbyniela.service.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private JwtService jwtService;
	private UserDetailsServiceImp userDetailsServiceImp;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		//1. get the authorization header of the request
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;
		//2. validate if the header exist and has the needed format
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		//3. extract the token 
		jwt = authHeader.substring(7);
		//4. extract the email from the token
		userEmail = jwtService.extractUsername(jwt);
		//5. Security validation
		// - email not null
		// - user is not authenticated
		if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
		}
		
	}

}
