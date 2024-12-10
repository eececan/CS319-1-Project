package com.project.btoproject.service;

import com.project.btoproject.model.UserTask;
import com.project.btoproject.repository.IUserTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskService implements IUserTaskService{

    private final IUserTaskRepository userTaskRepository;

    @Override
    public void saveTask(UserTask userTask) {
        userTaskRepository.save(userTask);
    }

    @Override
    public void deleteTask(int taskId) {
        userTaskRepository.deleteById(taskId);
    }

    @Override
    public List<UserTask> getTasks() {
        return userTaskRepository.findAll();
    }

    @Override
    public UserTask getTask(int taskId) {
        return userTaskRepository.findById(taskId).get();
    }

    @Override
    public void deleteTasks() {
        userTaskRepository.deleteAll();
    }

    @Override
    public void markAsCompleted(int taskId) {
        UserTask userTask = userTaskRepository.findById(taskId).get();
        userTask.setState(true);
        userTaskRepository.save(userTask);
    }

    @Override
    public void markAsUncompleted(int taskId) {
        UserTask userTask = userTaskRepository.findById(taskId).get();
        userTask.setState(false);
        userTaskRepository.save(userTask);
    }
}
