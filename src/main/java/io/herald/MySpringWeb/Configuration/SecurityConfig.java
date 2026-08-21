package io.herald.MySpringWeb.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Core Spring Security configuration class.
 * Configures authentication filters, password encoding, and HTTP request authorization rules.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Declares the BCrypt password encoder bean.
     * Used across the application to hash and verify passwords securely.
     * @return PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the main security filter chain.
     * Defines which endpoints are public and which require authentication.
     * @param http The HttpSecurity context.
     * @return The configured SecurityFilterChain.
     * @throws Exception on configuration errors.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection (common in stateless REST APIs)
        http.csrf(csrf -> csrf.disable());
        
        // Configure authorization rules for endpoints
        http.authorizeHttpRequests(auth -> auth
            // Allow public access to home, login, signup, and static resources
            .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
            // Allow public access to authentication REST APIs (e.g. login endpoint)
            .requestMatchers("/api/auth/**").permitAll()
            // Require authentication for all other /api/ endpoints
            .requestMatchers("/api/**").authenticated()
            // Allow everything else (e.g. Thymeleaf views) based on controller logic
            .anyRequest().permitAll()
        );
            
        // Inject the custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        // This ensures the token is validated before Spring attempts normal authentication
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
