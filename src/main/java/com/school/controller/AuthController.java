package com.school.controller;

import com.school.dto.ChangePasswordRequest;
import com.school.dto.JwtResponse;
import com.school.dto.LoginRequest;
import com.school.repository.UserRepository;
import com.school.security.JwtUtils;
import com.school.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication.
 * Handles user login and JWT token generation.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * @param loginRequest The login request containing username and password.
     * @return A ResponseEntity containing the JWTResponse (Token, ID, Username, Role).
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // 1. Authenticate credentials using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Set the authentication in the Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. Generate JWT Token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Get User Details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        // Get User ID from DB (Optimization: could be in UserDetails)
        Long userId = userRepository.findByUsername(userDetails.getUsername()).get().getId();

        // 5. Return Response
        return ResponseEntity.ok(new JwtResponse(jwt,
                userId,
                userDetails.getUsername(),
                role));
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@jakarta.validation.Valid @RequestBody ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        com.school.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Invalid old password!");
        }
        
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok("Password changed successfully!");
    }
}
