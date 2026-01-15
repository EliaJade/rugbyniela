package rugbyniela.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutService logoutService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(
				auth->auth
				.requestMatchers(
                        "/auth/**",           // Login/Register
                        "/doc/**", "/v3/api-docs/**", // Swagger
                        "/public/**"          // Cualquier cosa pública
						).permitAll()
						.anyRequest().authenticated()//everything else required authentication
					)
		.sessionManagement(sess->sess
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class)
		.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutService)
                .logoutSuccessHandler((request, response, authentication) -> 
                    SecurityContextHolder.clearContext()
                )
            );
		
		return http.build();
	}
}
