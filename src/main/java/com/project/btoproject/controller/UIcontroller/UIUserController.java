package com.project.btoproject.controller.UIcontroller;
import com.project.btoproject.model.User;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
public class UIUserController {
    private final IAllUsersService allUsersService;
    private final IUserService userService;

    public UIUserController(IAllUsersService userService, IUserService allUsersService) {
        this.allUsersService = userService;
        this.userService = allUsersService;
    }
    @GetMapping("/getAllUsers")
    public String getUsersPage(Model model) {
        List<User> users = allUsersService.getAllUsers();
        model.addAttribute("all_users", users);
        return "member-list";
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
