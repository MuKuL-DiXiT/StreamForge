package com.stream.streamforge.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.stream.streamforge.dto.LoginRequest;
import com.stream.streamforge.dto.LoginResponse;
import com.stream.streamforge.dto.SignupRequest;
import com.stream.streamforge.dto.SignupResponse;
import com.stream.streamforge.model.UserTable;
import com.stream.streamforge.repository.UserRepository;
@Service
public class AuthService {

    public final UserRepository userRepo;
    public final PasswordEncoder passwordEncoder;
    public final JwtService jwtService;

    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public boolean exists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public SignupResponse signupService(SignupRequest req) {
        String email = req.getEmail();
        if (exists(email)) {
            throw new RuntimeException("Email already exists");
        }

        String role = req.getRole();
        if (!("USER".equals(role) || "CREATOR".equals(role))) {
            throw new RuntimeException("Invalid role");
        }

        UserTable user = new UserTable();
        user.setEmail(email);
        user.setName(req.getName());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);

        return new SignupResponse(user.getId(), user.getEmail(), user.getRole());
    }

    public LoginResponse loginService(LoginRequest req) {
        String email = req.getEmail();
        String rawPassword = req.getPassword();
        UserTable user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        String token = jwtService.generateToken(email, user.getRole());
        return new LoginResponse(user.getId(), user.getEmail(), token);
    }
}
