package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UIUserListController {
    private final UserService userService;

    public UIUserListController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public String getUsersPage(Model model) {
        // Fetch users from the service
        List<UserEntity> users = userService.getAllUsers();
        // Add users to the model
        model.addAttribute("users", users);
        // Return the name of the HTML view
        return "member-list";
    }
}
