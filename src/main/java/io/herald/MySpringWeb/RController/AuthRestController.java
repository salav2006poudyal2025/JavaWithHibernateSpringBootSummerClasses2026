package io.herald.MySpringWeb.RController;

import io.herald.MySpringWeb.Configuration.JWUtil;
import io.herald.MySpringWeb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller responsible for handling stateless authentication requests.
 * Specifically, issues JWT tokens for valid login attempts.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWUtil jwtUtil;

    /**
     * Authenticates a user and generates a JWT token.
     * @param username The username attempting login.
     * @param password The raw password.
     * @return ResponseEntity containing the JWT token if successful, or 401 UNAUTHORIZED if invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        
        // Verify credentials using BCrypt hashing in the service layer
        if (userService.authenticate(username, password)) {
            // Generate token upon successful authentication
            String token = jwtUtil.generateToken(username);
            
            // Construct JSON response with the token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            
            return ResponseEntity.ok(response);
        } else {
            // Reject unauthorized access
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
