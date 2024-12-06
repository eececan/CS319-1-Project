package com.project.btoproject.service;

import com.project.btoproject.dto.AuthResponseDTO;
import com.project.btoproject.dto.ErrorResponseDto;
import com.project.btoproject.dto.LoginDto;
import com.project.btoproject.dto.RegisterDto;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.repository.UserRepository;
import com.project.btoproject.security.JWTGenerator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.Locale;

@Service
//@RequiredArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));

            // Set the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String token = jwtGenerator.generateToken(authentication);

            // Return success response
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            // Handle specific exceptions and return meaningful messages
            if (e instanceof org.springframework.security.authentication.BadCredentialsException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponseDto("Invalid username or password"));
            } else {
                // For other exceptions, return a generic error message
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponseDto("Authentication failed. Please try again."));
            }
        }
    }

    public String register(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return ("Username is taken!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        String role = registerDto.getRole();
        Role roles = roleRepository.findByName(role.toUpperCase(Locale.ROOT)).get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);

        return ("User registered success!");
    }

}
