package rugbyniela.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(
				auth->auth
				.requestMatchers(
                        "/auth/**",           // Login/Register
                        "/doc/**", "/v3/api-docs/**", // Swagger
                        "/user/register"         // Cualquier cosa pública
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
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir explícitamente al frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); 
        
        // Permitir todos los métodos (GET, POST, PUT, DELETE, OPTIONS)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Permitir todas las cabeceras (Authorization, Content-Type, etc)
        configuration.setAllowedHeaders(List.of("*"));
        
        // Importante si usas cookies o tokens en cabeceras
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
