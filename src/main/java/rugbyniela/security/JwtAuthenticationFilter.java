package rugbyniela.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import rugbyniela.service.UserDetailsServiceImp;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	private final UserDetailsServiceImp userDetailsServiceImp;

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
			//fetch user by its email from the data base
			UserDetails userDetails = this.userDetailsServiceImp.loadUserByUsername(userEmail);
			//validate that the token is valid and belong to the user
			if(jwtService.isTokenValid(jwt, userDetails)) {
				//create a spring's authentication object
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,
						null,//we don't need credentials becuas it has already entered a token
						userDetails.getAuthorities());
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request));
				//authenticate the user, set it to the security spring context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
