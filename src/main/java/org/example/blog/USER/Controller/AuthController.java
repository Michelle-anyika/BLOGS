package org.example.blog.USER.Controller;

import lombok.RequiredArgsConstructor;
import org.example.blog.JWT.JwtUtil;
import org.example.blog.USER.DTO.UserDTO;
import org.example.blog.USER.Model.User;
import org.example.blog.USER.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(userDTO.getEmail()))) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = User.builder()
//                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        // Authenticate using email + password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
        );

        // Fetch user from DB
        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(userDTO.getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        // Return token in JSON object
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }
}
