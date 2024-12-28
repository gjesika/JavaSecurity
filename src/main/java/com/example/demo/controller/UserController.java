package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Endpoint për regjistrim të përdoruesve
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user, @RequestParam boolean isAdmin) {
        userService.registerUser(user, isAdmin); // Regjistrimi i përdoruesit
        return ResponseEntity.ok("User registered successfully!");
    }

    // Endpoint për logimin dhe marrjen e JWT token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        // Gjej përdoruesin nga baza e të dhënave përmes email-it
        User user = userService.findByEmail(email);

        // Thjesht kthe tokenin nëse përdoruesi ekziston, pa kontrolluar fjalëkalimin
        if (user != null) {
            // Gjenero tokenin JWT nëse përdoruesi ekziston ose nuk ndryshon
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok("Bearer " + token);  // Kthe tokenin JWT në përgjigje
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");  // Gabim nëse përdoruesi nuk ekziston
        }
    }


    @PreAuthorize("hasRole('ADMIN')")  // Spring Security handles this check
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<String> promoteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String role = jwtUtil.extractRole(token);  // Merrni rolin nga tokeni
        System.out.println("Role extracted from token: " + role);  // Log për të kontrolluar rolin

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(403).body("Forbidden: You do not have admin privileges.");
        }

        userService.promoteUser(id);
        return ResponseEntity.ok("User promoted to admin.");
    }



}








