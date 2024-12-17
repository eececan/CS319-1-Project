
package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.AuthService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ui/UserProfile")
public class UIUserProfileController {


    private final AllUsersService allUsersService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    UIUserProfileController(AllUsersService allUsersService, UserService userService, PasswordEncoder passwordEncoder, AuthService authService) {
        this.allUsersService = allUsersService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, RedirectAttributes redirectAttributes,
                             @ModelAttribute("successMessage") String successMessage,
                             @ModelAttribute("errorMessage") String errorMessage) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username));

        // Add flash attributes to the model
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("user", user);

        // Check user roles and return the respective profile page
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            return "director-profile"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "advisor-profile"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            Guide guide = (Guide) user;
            List<Event> event = guide.getEvents();
            // Add user to the model so that it's accessible in the view
            int sum = 0;
            for (int i = 0; i < event.size(); i++) {
                System.out.println(event.get(i).getId());
                if (event.get(i).getEventType() == EventType.TOUR) {
                    if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                        sum++;
                    }
                }
            }
            model.addAttribute("sum", sum);
            return "guide-profile"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            return "head-secretary-profile"; // Head Secretary's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            GuideInTraining guide = (GuideInTraining) user;
            List<Event> event = guide.getEvents();
            // Add user to the model so that it's accessible in the view
            int sum = 0;
            for (int i = 0; i < event.size(); i++) {
                if (event.get(i).getEventType() == EventType.TOUR) {
                    if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                        sum++;
                    }
                }
            }
            model.addAttribute("sum", sum);
            return "guide-in-training-profile";
        } else {
            return "page-empty"; // Default page for unrecognized roles
        }
    }


    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam Map<String, Object> dtoMap, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        allUsersService.updateProfile(Long.parseLong(username), dtoMap);
        return "redirect:/ui/UserProfile/profile";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "change-password";
    }

    @PostMapping("/changePasswordBackend")
    public String changePasswordBackend(
            @RequestParam("newPassword1") String newPassword1,
            @RequestParam("newPassword2") String newPassword2,
            RedirectAttributes redirectAttributes
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        if (newPassword1.equals(newPassword2)) {
            userService.changePassword(Long.parseLong(username), newPassword1);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
            return "redirect:/ui/UserProfile/profile"; // Redirect to a success page or profile page
        } else {
            // Handle errors (e.g., wrong current password)
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/ui/UserProfile/changePassword"; // Redirect back to the form
        }
    }


}
