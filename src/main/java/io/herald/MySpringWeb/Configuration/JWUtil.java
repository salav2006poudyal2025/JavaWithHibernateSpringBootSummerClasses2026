package io.herald.MySpringWeb.Configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for generating, parsing, and validating JSON Web Tokens (JWT).
 */
@Component
public class JWUtil {

    // The secret key used for HMAC signing, retrieved from properties
    @Value("${jwt.secret}")
    private String secret;

    // The expiration time of the token in milliseconds
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Generates a cryptographic key from the configured secret string.
     * @return The signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts the username (subject) from the token.
     * @param token The JWT string.
     * @return The extracted username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the token.
     * @param token The JWT string.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Helper method to extract a specific claim from the token.
     * @param token The JWT string.
     * @param claimsResolver Function to extract the desired claim.
     * @return The resolved claim object.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT to retrieve all embedded claims.
     * @param token The JWT string.
     * @return Claims object containing token payload.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks whether a token has passed its expiration time.
     * @param token The JWT string.
     * @return True if expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Public entry point to generate a new JWT for a specific user.
     * @param username The subject of the token.
     * @return The generated JWT string.
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Assembles and cryptographically signs the JWT.
     * @param claims Custom payloads (empty by default).
     * @param subject The username.
     * @return The finalized JWT string.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set token issuance time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set expiration time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign using HS256 and secret key
                .compact();
    }

    /**
     * Validates a token against a specific username and checks expiration.
     * @param token The JWT string.
     * @param username The username attempting authentication.
     * @return True if token is valid and matches the user, false otherwise.
     */
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
