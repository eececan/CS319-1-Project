package com.project.btoproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.dto.*;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.repository.UserRepository;
import com.project.btoproject.security.JWTGenerator;
import com.project.btoproject.service.AuthService;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final IUserService userService;

    @Autowired
    public AuthController(AuthService _authService, IUserService userService) {
        this.authService = _authService;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto,  HttpServletRequest request) {
        ResponseEntity<?> response = authService.login(loginDto, request);
        if (response.getStatusCode().is2xxSuccessful()) {
            AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();
            return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
        }
        return ResponseEntity.status(response.getStatusCode())
                .body(response.getBody());
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        HttpStatus status = response.equals("Username is taken!") ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("addUser")
    public String addUser(@RequestBody Map<String, Object> dtoMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = new UserEntity();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String userId = userDetails.getUsername();
                String password = userDetails.getPassword();

                try {
                    user.setId(Long.parseLong(userId));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid user ID: " + userId);
                    return "Invalid user ID";
                }
                user.setPassword(password);
                System.out.println("User ID: " + userId);
                System.out.println("Password: " + password);
            } else {
                System.out.println("Principal is not an instance of UserDetails. Principal: " + principal);
                return "Authentication error";
            }

            // Handle roles and deserialize the DTO accordingly
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUIDE"))) {
                    UserGuideDto userGuideDto = objectMapper.convertValue(dtoMap, UserGuideDto.class);
                    userService.enterPersonalInformationGuide(user, userGuideDto);
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADVISOR"))) {
                    UserAdvisorDto userAdvisorDto = objectMapper.convertValue(dtoMap, UserAdvisorDto.class);
                    userService.enterPersonalInformationAdvisor(user, userAdvisorDto);
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUIDE_IN_TRAINING"))) {
                    UserGuideInTrainingDto userGuideInTrainingDto = objectMapper.convertValue(dtoMap, UserGuideInTrainingDto.class);
                    userService.enterPersonalInformationGuideInTraining(user, userGuideInTrainingDto);
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HEAD_SECRETARY"))) {
                    System.out.println("User is a HEAD SECRETARY");
                    // Add logic for HEAD_SECRETARY
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DIRECTOR"))) {
                    System.out.println("User is a DIRECTOR");
                    // Add logic for DIRECTOR
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COORDINATOR"))) {
                    UserCoordinatorDto coordinatorDto = objectMapper.convertValue(dtoMap, UserCoordinatorDto.class);
                    userService.enterPersonalInformationCoordinator(user, coordinatorDto);
                } else {
                    System.out.println("User has no matching roles.");
                    // Add logic for unmatched roles
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to deserialize request body: " + e.getMessage());
                return "Invalid request body";
            }
        } else {
            System.out.println("No authenticated user found.");
            return "Authentication required";
        }
        return "User information updated successfully";
    }



    /*
    public String addCoordinator(UserEntity user){

    }

    public String addHeadSecretary(){

    }

    public String addDirector(){

    }*/
}