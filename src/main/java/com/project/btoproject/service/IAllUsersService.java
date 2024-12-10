package com.project.btoproject.service;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;

import java.util.List;

public interface IAllUsersService {
    List<User> getAllUsers();
    void addUser(User user);
    User getUserById(Long userId);
    List<UserTask> seeAllTasks(User user);
    void addTaskToUser(User user, UserTask newTask);
}
