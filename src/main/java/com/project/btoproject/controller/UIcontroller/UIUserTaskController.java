package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;
import com.project.btoproject.service.AllUsersService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
@RequestMapping("ui/task")
public class UIUserTaskController {
    private final AllUsersService allUsersService;
    UIUserTaskController(AllUsersService allUsersService) {
        this.allUsersService = allUsersService;
    }
    @GetMapping("/todo")
    public String getTodoPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username));
        List<UserTask> tasks = allUsersService.seeAllTasks(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user); // This will allow you to display user details on the frontend
        //model.addAttribute("userId", userId);
        return "project-todo";
    }

}
