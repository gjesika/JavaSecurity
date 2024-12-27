package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Regjistrimi i përdoruesit
    public User registerUser(User user, boolean isAdmin) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already in use");
        }
        user.setRole(isAdmin ? "ADMIN" : "USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Enkodoni fjalëkalimin
        return userRepository.save(user);  // Ruani përdoruesin në databazë
    }

    // Gjej përdoruesin nga emaili
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Promovo përdoruesin në admin
    public void promoteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole("ADMIN");
        userRepository.save(user);
    }
}
