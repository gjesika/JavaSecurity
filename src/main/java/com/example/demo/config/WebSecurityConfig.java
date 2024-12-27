package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // Krijoni një SecurityFilterChain që përdor konfigurimet e reja për Spring Security 6.1+
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Definimi i politikave të autorizimit dhe sigurisë
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register", "/api/login","api/users/{id}/promote").permitAll()  // Lejo pa autentifikim për register dhe login
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Vetëm admin mund të aksesojnë këto endpoint
                        .anyRequest().authenticated()  // Të gjitha kërkesat e tjera kërkojnë autentifikim
                )
                .csrf(csrf -> csrf.disable())  // Çaktivizo CSRF për aplikacione API që përdorin JWT
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // Shtoni filterin JWT

        return http.build();  // Krijo dhe kthe konfigurimin e sigurisë
    }

    // Krijimi i një PasswordEncoder për enkodimin e fjalëkalimeve
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Përdorimi i BCrypt për enkodimin e fjalëkalimeve
    }
}



