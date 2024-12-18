package com.project.btoproject.service;

import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.User;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.model.UserTask;

import java.util.List;
import java.util.Map;

public interface IAllUsersService {
    List<User> getAllUsers();
    void addUser(User user);
    void deleteUserById(Long id);
    User getUserById(Long userId);
    List<UserTask> seeAllTasks(User user);
    void addTaskToUser(User user, UserTask newTask);
    boolean updateTaskStatus(User user, Long taskId, boolean b);
    void changeRole(Long userId, String role);
    boolean hasMissingInformation(User user, UserEntity userEntity);
    boolean hasUserWithId(Long id);
    void updateProfile(Long id, Map<String, Object> dtoMap);
}
