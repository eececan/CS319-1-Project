package com.project.btoproject.service;

import com.project.btoproject.model.UserTask;

import java.util.List;

public interface IUserTaskService {
    void saveTask(UserTask userTask);
    void deleteTask(int taskId);
    List<UserTask> getTasks();
    UserTask getTask(int taskId);
    void deleteTasks();
    void markAsCompleted(int taskId);
    void markAsUncompleted(int taskId);
}
