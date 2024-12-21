package com.project.btoproject.controller.UIcontroller;
import com.project.btoproject.dto.UserDto;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.User;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UIUserController {
    private final IAllUsersService allUsersService;
    private final IUserService userService;

    public UIUserController(IAllUsersService userService, IUserService allUsersService) {
        this.allUsersService = userService;
        this.userService = allUsersService;
    }

    @GetMapping("/getAllUsers")
    public String getUsersPage(
            Model model,
            @ModelAttribute("successMessage") String successMessage,
            @RequestParam(required = false) String roleFilter) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String roleUser = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            roleUser = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_UNKNOWN");
        }

        User userE = allUsersService.getUserById(Long.parseLong(username));
        List<User> users = allUsersService.getAllUsers();
        List<UserDto> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDto userDTO = new UserDto();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());

            // Fetch the UserEntity and determine the role
            UserEntity userEntity = userService.findUserByUsername(user.getId()).get();
            List<Role> roles = userEntity.getRoles(); // Retrieve the list of roles
            String role = roles != null && !roles.isEmpty() ? roles.get(0).getName() : null;
            userDTO.setRole(role);
            userDTO.setPicture(user.getPicture());
            userDTO.setDescription(user.getDescription());
            userDTO.setStartDate(user.getStartDate());

            // Add to list only if roleFilter matches or no filter is applied
            if (roleFilter == null || roleFilter.isEmpty() || role.equals(roleFilter)) {
                userDTOs.add(userDTO);
            }
        }

        model.addAttribute("all_users", userDTOs);
        model.addAttribute("user", userE);
        model.addAttribute("role", roleUser);
        model.addAttribute("roleFilter", roleFilter); // Pass the filter to the view

        // Pass success message to the model (if present)
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            model.addAttribute("roleUser", roleUser);
            return "member-list";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE")) ||
                authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            model.addAttribute("roleUser", roleUser);
            return "member-list";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            roleUser = "ADVISOR";
            model.addAttribute("roleUser", roleUser);
            return "member-list";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
            model.addAttribute("roleUser", "ROLE_COORDINATOR");
            return "member-list";
        } else {
            roleUser = "HEAD SECRETARY";
            model.addAttribute("roleUser", roleUser);
            return "member-list";
        }
    }



    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        try {
            userService.deleteUserByUsername(id);
        } catch (Exception e) {
            // Log the exception and rethrow for debugging purposes
            System.err.println("Error while deleting user: " + e.getMessage());
            e.printStackTrace();
            throw e; // Optional: rethrow if you want it to propagate
        }
        return "redirect:/getAllUsers";
    }
}
