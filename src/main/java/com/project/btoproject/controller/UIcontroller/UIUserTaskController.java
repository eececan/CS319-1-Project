package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.UserService;
import com.project.btoproject.service.UserTaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("ui/task")
public class UIUserTaskController {
    private final AllUsersService allUsersService;
    private final UserService userService;

    // Constructor to inject services
    UIUserTaskController(AllUsersService allUsersService, UserService userService) {
        this.allUsersService = allUsersService;
        this.userService = userService;
    }

    // Get tasks page (To-Do List)
    @GetMapping("/todo")
    public String getTodoPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String roleUser="";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();
        List<UserTask> tasks = allUsersService.seeAllTasks(user);
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            return"project-todo-DirectorSpecific";
        }
        else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE")) || (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING")) )) {
            return"project-todo-guideSpecific";
        }
        else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            roleUser ="ADVISOR";
            model.addAttribute("roleUser", roleUser);
            return"project-todo";
        }
        else{
            roleUser ="HEAD SECRETARY";
            model.addAttribute("roleUser", roleUser);
            return "project-todo";
        }
    }

    // Post a new task (UI for adding tasks)
    @PostMapping("/post-task")
    public String postTask(@RequestParam String taskName, @RequestParam String description, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();

        UserTask newTask = new UserTask();
        newTask.setTaskName(taskName);  // Set task name
        newTask.setTaskDescription(description); // Set description
        newTask.setTaskDeadline(new Date());
        // Add task to user
        allUsersService.addTaskToUser(user, newTask);
        return "redirect:/ui/task/todo"; // Redirect to task list page
    }


    // Delete a task from user
    @GetMapping("/delete-task/{taskId}")
    public String deleteTask(@PathVariable Long taskId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();

        boolean taskDeleted = allUsersService.deleteTaskFromUser(user, taskId);
        if (taskDeleted) {
            model.addAttribute("message", "Task deleted successfully.");
        } else {
            model.addAttribute("message", "Task not found.");
        }

        return "redirect:/ui/task/todo"; // Redirect to task list page
    }

    // Mark a task as complete
    @GetMapping("/mark-task-complete/{taskId}")
    public String markTaskAsComplete(@PathVariable Long taskId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();

        boolean marked = allUsersService.updateTaskStatus(user, taskId, true);
        if (marked) {
            model.addAttribute("message", "Task marked as complete.");
        } else {
            model.addAttribute("message", "Task not found.");
        }

        return "redirect:/ui/task/todo"; // Redirect to task list page
    }

    // Mark a task as incomplete
    @GetMapping("/mark-task-incomplete/{taskId}")
    public String markTaskAsIncomplete(@PathVariable Long taskId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();

        boolean marked = allUsersService.updateTaskStatus(user, taskId, false);
        if (marked) {
            model.addAttribute("message", "Task marked as incomplete.");
        } else {
            model.addAttribute("message", "Task not found.");
        }

        return "redirect:/ui/task/todo"; // Redirect to task list page
    }

}
