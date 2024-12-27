package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_KEY = "your-secret-key";  // Këtu mund të përdorni një sekret të sigurt dhe të ruajtur mirë
    private static final String TOKEN_PREFIX = "Bearer ";  // Për prefixin e JWT
    private static final String HEADER_STRING = "Authorization";  // Emri i header-it për JWT në kërkesë

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Merrni token-in nga header-i i kërkesës
        String token = request.getHeader(HEADER_STRING);

        // Verifikoni nëse token-i është i pranishëm dhe ka prefixin e saktë
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            try {
                // Heqni prefixin "Bearer "
                token = token.replace(TOKEN_PREFIX, "");

                // Dekodoni dhe verifikoni token-in JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)  // Ky është çelësi për verifikimin e token-it
                        .parseClaimsJws(token)
                        .getBody();

                // Merrni username nga claims dhe krijoni një objekt UsernamePasswordAuthenticationToken
                String username = claims.getSubject();

                if (username != null) {
                    // Krijo një objekt të autenticitetit dhe vendoseni në SecurityContext
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Gabim gjatë verifikimit të JWT
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Mund të vendosni një status gabimi të përshtatshëm
            }
        }

        // Vazhdoni me filtrin e mëpasshëm
        filterChain.doFilter(request, response);
    }
}


