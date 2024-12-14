
package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("ui/UserProfile")
public class UIUserProfileController {


    private final AllUsersService allUsersService;
    private final UserService userService;

    UIUserProfileController(AllUsersService allUsersService, UserService userService) {
        this.allUsersService = allUsersService;
        this.userService = userService;
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
           int sum=0;
            for (int i = 0; i < event.size(); i++) {
                System.out.println(event.get(i).getId());
                if(event.get(i).getEventType() == EventType.TOUR ){
                    System.out.println("here");
                    if((event.get(i).getStatus() == Status.COMPLETED_TOUR)){
                        System.out.println("even here");
                        sum++;
                    }

                }
            }
            System.out.println(sum);
            model.addAttribute("sum", sum);
            return "guide-profile"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            return "head-secretary-profile"; // Head Secretary's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            GuideInTraining guide = (GuideInTraining) user;
            List<Event> event = guide.getEvents();
            // Add user to the model so that it's accessible in the view
            int sum=0;
            for (int i = 0; i < event.size(); i++) {
                System.out.println(event.get(i).getId());
                if(event.get(i).getEventType() == EventType.TOUR ){
                    System.out.println("here");
                    if((event.get(i).getStatus() == Status.COMPLETED_TOUR)){
                        System.out.println("even here");
                        sum++;
                    }

                }
            }
            System.out.println(sum);
            model.addAttribute("sum", sum);
            return"guide-in-training-profile";
        }
        else
            return "page-empty"; // Default page for unrecognized roles
    }
}
