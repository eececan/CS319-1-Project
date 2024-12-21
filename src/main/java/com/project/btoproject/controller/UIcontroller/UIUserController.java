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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            @ModelAttribute("errorMessage") String errorMessage,
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

        User userE = allUsersService.getUserById(Long.parseLong(username)).get();
        List<User> users = allUsersService.getAllUsers();
        List<UserDto> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDto userDTO = new UserDto();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhoneNumber(user.getPhoneNumber());

            UserEntity userEntity = userService.findUserByUsername(user.getId()).get();
            List<Role> roles = userEntity.getRoles();
            String role = roles != null && !roles.isEmpty() ? roles.get(0).getName() : null;
            userDTO.setRole(role);
            userDTO.setPicture(user.getPicture());
            userDTO.setDescription(user.getDescription());
            userDTO.setStartDate(user.getStartDate());

            if (roleFilter == null || roleFilter.isEmpty() || role.equals(roleFilter)) {
                userDTOs.add(userDTO);
            }
        }
/*
        List<UserEntity> userEntities = userService.getAllUserEntities();
        for (UserEntity userEntity : userEntities) {
            Optional<User> all_user = allUsersService.getUserById(Long.parseLong(username));
            if (all_user.isEmpty()) {
                UserDto userDTO = new UserDto();
                userDTO.setId(Long.parseLong(userEntity.getUsername()));
                String roleName = userEntity.getRoles().stream()
                        .findFirst()
                        .map(Role::getName)
                        .orElse(null);
                userDTO.setRole(roleName);
                userDTOs.add(userDTO);
            }
        }
*/
        model.addAttribute("all_users", userDTOs);
        model.addAttribute("user", userE);
        model.addAttribute("role", roleUser);
        model.addAttribute("roleFilter", roleFilter);

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
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

    @GetMapping("/redirectToUsersPage")
    public String redirectToUsersPageWithError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "This user has not logged in and entered their information yet! Please try to view their personal information later!");
        return "redirect:/getAllUsers";
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
