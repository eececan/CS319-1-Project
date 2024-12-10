package com.project.btoproject.controller;

import com.project.btoproject.model.*;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.UserService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AllUsersService allUsersService;

    @Autowired
    public UserController(AllUsersService allUsersService) {
        this.allUsersService = allUsersService;
    }

    @GetMapping("/get-tasks")
    public List<UserTask> getTasksOfGuide(@RequestParam Long userId) {
        User user = allUsersService.getUserById(userId);
        return allUsersService.seeAllTasks(user);
    }

    @PostMapping("/post-tasks")
    public ResponseEntity<String> postTask(@RequestParam Long userId, @RequestBody UserTask newTask) {
        User user = allUsersService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        allUsersService.addTaskToUser(user, newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created successfully.");
    }

    @DeleteMapping("/delete-task")
    public ResponseEntity<String> deleteTask(@RequestParam Long userId, @RequestParam Long taskId) {
        User user = allUsersService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean taskDeleted = allUsersService.deleteTaskFromUser(user, taskId);
        if (!taskDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @PatchMapping("/mark-task-complete")
    public ResponseEntity<String> markTaskAsComplete(@RequestParam Long userId, @RequestParam Long taskId) {
        User user = allUsersService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean marked = allUsersService.updateTaskStatus(user, taskId, true);
        if (!marked) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        return ResponseEntity.ok("Task marked as complete.");
    }

    @PatchMapping("/mark-task-incomplete")
    public ResponseEntity<String> markTaskAsIncomplete(@RequestParam Long userId, @RequestParam Long taskId) {
        User user = allUsersService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        boolean marked = allUsersService.updateTaskStatus(user, taskId, false);
        if (!marked) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
        return ResponseEntity.ok("Task marked as incomplete.");
    }

}
