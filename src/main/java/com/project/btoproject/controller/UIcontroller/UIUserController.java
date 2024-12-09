package com.project.btoproject.controller.UIcontroller;
import com.project.btoproject.model.User;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
public class UIUserController {
    private final IAllUsersService userService;

    public UIUserController(IAllUsersService userService) {
        this.userService = userService;
    }
    @GetMapping("/getAllUsers")
    public String getUsersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("all_users", users);
        return "member-list";
    }
}