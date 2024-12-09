package com.project.btoproject.service;

import com.project.btoproject.model.User;

import java.util.List;

public interface IAllUsersService {
    List<User> getAllUsers();
    void addUser(User user);
}
