package com.example.demo.security;

import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "a_very_secure_and_long_secret_key_which_is_at_least_64_characters_long_for_HS512";

    // Function to encode secret key to Base64url
    private static String encodeBase64Url(String secretKey) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretKey.getBytes());
    }

    private static final String ENCODED_SECRET_KEY = encodeBase64Url(SECRET_KEY);

    // Generate the JWT token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())  // Add role claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Token expires in 1 hour
                .signWith(SignatureAlgorithm.HS512, ENCODED_SECRET_KEY)
                .compact();
    }

    // Extract the role from the JWT token
    public String extractRole(String token) {
        try {
            // Remove the "Bearer " prefix if it exists
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);  // Removes the first 7 characters ("Bearer ")
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(ENCODED_SECRET_KEY)
                    .parseClaimsJws(token)  // Now, token is the actual JWT
                    .getBody();
            return claims.get("role", String.class);  // Retrieve role from claim
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

}