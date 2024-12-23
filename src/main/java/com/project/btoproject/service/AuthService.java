package com.project.btoproject.service;

import com.project.btoproject.dto.*;
import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.User;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.repository.UserRepository;
import com.project.btoproject.security.JWTGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final IAllUsersRepository allUsersRepository;

    public ResponseEntity<?> login(LoginDto loginDto, HttpServletRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));

            // Set the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Explicitly store the SecurityContext in the session
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Generate JWT token
            String token = jwtGenerator.generateToken(authentication);

            // Return success response
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (Exception e) {
            if (e instanceof org.springframework.security.authentication.BadCredentialsException) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponseDto("Invalid username or password"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponseDto("Authentication failed. Please try again."));
            }
        }
    }

    @Transactional
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return "Username is already taken!";
        }

        Role role = roleRepository.findByName(registerDto.getRole().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + registerDto.getRole()));

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return "User registered successfully!";
    }

    @Transactional
    public String registerAdvisor(AdvisorRegisterDto registerDto) {
        // Check if the username is already taken
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return "Username is already taken!";
        }

        // Validate the role and retrieve it
        Role role = roleRepository.findByName(registerDto.getRole().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + registerDto.getRole()));

        // Create and populate the UserEntity
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singletonList(role));

        // Create and populate the Advisor
        Advisor advisor = new Advisor();
        advisor.setId(Long.parseLong(registerDto.getUsername())); // Assuming ID is based on username
        advisor.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        advisor.setStartDate(new Date());
        try {
            // Parse and validate the responsible day
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(registerDto.getResponsibleDay().trim().toUpperCase(Locale.ENGLISH));
            advisor.setResponsibleDay(dayOfWeek);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid responsible day: " + registerDto.getResponsibleDay(), e);
        }

        // Save the user and advisor entities
        userRepository.save(user);
        allUsersRepository.save(advisor);

        return "User registered successfully!";
    }

}
