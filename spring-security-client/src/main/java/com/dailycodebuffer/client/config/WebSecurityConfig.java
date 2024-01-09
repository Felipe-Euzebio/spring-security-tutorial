package com.dailycodebuffer.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
    private static final String[] WHITE_LIST_URLS = {
        "/hello",
        "/register",
        "/verifyRegistration*",
        "/resendVerifyToken*"
    };

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}

    /*
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    */

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {		
		http
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
    	
    	http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(WHITE_LIST_URLS)
                	.permitAll()
                .requestMatchers("/api/**")
	                .permitAll()
	                .anyRequest()
	                .authenticated()
            );
    	
    	return http.build();
    }
	
}
