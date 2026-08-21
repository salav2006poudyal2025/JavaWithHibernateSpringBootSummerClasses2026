package io.herald.MySpringWeb.Configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Custom JWT Authentication Filter that intercepts HTTP requests to validate JWT tokens.
 * It extends OncePerRequestFilter to guarantee a single execution per request dispatch.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWUtil jwtUtil;

    /**
     * Performs the actual filtering logic.
     * Extracts the JWT from the Authorization header and validates it.
     * If valid, it populates the Spring SecurityContext with the user's authentication details.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if header contains a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                // Parse the token to extract the username
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                System.out.println("JWT Parsing Error: " + e.getMessage());
            }
        }

        // If a username was extracted and there's no existing authentication in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Validate the token's integrity and expiration
            if (jwtUtil.validateToken(jwt, username)) {
                // Create an authentication token (no authorities mapped in this implementation)
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                
                // Set the details based on the web request
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                // Set the authentication in the Security Context to mark the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
