package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.User;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIUserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        // Get the current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get the username from the authentication object
        User user = userService.getUserByUsername(username); // Fetch the user from the database

        // Add user to the model so that it's accessible in the view
        model.addAttribute("user", user);

        // Check user roles and return the respective profile page
        if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            return "director-profile"; // Director's profile page
        } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "advisor-profile"; // Advisor's profile page
        } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            return "guide-profile"; // Guide's profile page
        } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            return "head-secretary-profile"; // Head Secretary's profile page
        }
        else
        return "guide-in-training-profile";
    }
}
