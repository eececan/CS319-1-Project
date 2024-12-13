
package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.User;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIUserProfileController {


    private final AllUsersService allUsersService;

    UIUserProfileController(AllUsersService allUsersService) {
        this.allUsersService = allUsersService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username));
        // Add user to the model so that it's accessible in the view
        model.addAttribute("user", user);

        // Check user roles and return the respective profile page
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            return "director-profile"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "advisor-profile"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            return "guide-profile"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            return "head-secretary-profile"; // Head Secretary's profile page
        }
        else
        return "guide-in-training-profile";
    }
}
