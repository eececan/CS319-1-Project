package com.project.btoproject.service;

import com.project.btoproject.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final IAllUsersService allUsersService;
    private final EventService eventService;


    public String validateAndReturn(String role, Map<String, Object> dtoMap, Model model, String errorMessage, Long userId) {
        // Add the error message to the model
        model.addAttribute("errorMessage", errorMessage);

        // Preserve the form data to repopulate the view
        model.addAllAttributes(dtoMap);

        // Role-specific logic for returning to the profile view
        switch (role) {
            case "ROLE_DIRECTOR": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());

                    }
                }
                else{
                    Director user = new Director();
                    user.setId(userId);
                    model.addAttribute("user", user);
                }

                model.addAttribute("role", role);
                return "director-profile"; // View for editing the director's profile
            }
            case "ROLE_COORDINATOR": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());
                    }
                }
                else{
                    Coordinator user = new Coordinator();
                    user.setId(userId);
                    model.addAttribute("user", user);
                }

                model.addAttribute("role", role);
                return "director-profile"; // View for editing the coordinator's profile
            }
            case "ROLE_ADVISOR": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());
                    }
                }
                else{
                    Advisor user = new Advisor();
                    user.setId(userId);
                    model.addAttribute("user", user);
                }

                return "advisor-profile"; // View for editing the advisor's profile
            }
            case "ROLE_GUIDE": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());
                        model.addAttribute("sum", 0);
                    }
                }
                else{
                    Guide user = new Guide();
                    user.setId(userId);
                    model.addAttribute("user", user);
                    model.addAttribute("sum", 0);
                }

                 // Example additional attribute
                model.addAttribute("role", role);
                model.addAttribute("isUser", "false");
                return "guide-profile"; // View for editing the guide's profile
            }
            case "ROLE_GUIDE_IN_TRAINING": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());
                        model.addAttribute("sum", 0);
                        model.addAttribute("isUser", "true");

                    }
                }
                else{
                    GuideInTraining user = new GuideInTraining();
                    user.setId(userId);
                    model.addAttribute("user", user);
                    model.addAttribute("sum", 0);
                    model.addAttribute("isUser", "false");

                }
                // Example additional attribute
                model.addAttribute("role", role);
                return "guide-profile"; // View for editing the guide in training's profile
            }
            case "ROLE_HEAD_SECRETARY": {
                if(allUsersService.hasUserWithId(userId)){
                    Optional<User> user = allUsersService.getUserById(userId);
                    if(user.isPresent()){
                        model.addAttribute("user", user.get());
                        model.addAttribute("isUser", "true");

                    }
                }
                else{
                    HeadSecretary user = new HeadSecretary();
                    user.setId(userId);
                    model.addAttribute("user", user);
                    model.addAttribute("isUser", "false");
                }

                model.addAttribute("sum", 0); // Example additional attribute
                model.addAttribute("role", role);
                model.addAttribute("isUser", "false");
                return "guide-profile"; // View for editing the head secretary's profile
            }

        }
        return "false";
    }

    public String validatePersonalInfo(Map<String, Object> dtoMap) {
        // Example: Check if the email contains "bilkent"
        String email = (String) dtoMap.get("email");
        if (email == null || !email.contains("bilkent@edu.tr")) {
            return "The email you use must be a bilkent mail! Please enter your bilkent mail!";
        }
        // Other validation logic...
        return "true";
    }

}
