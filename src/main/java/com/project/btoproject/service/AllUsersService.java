package com.project.btoproject.service;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.IUserTaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllUsersService implements IAllUsersService {
    private final IAllUsersRepository repository;
    private final IUserTaskRepository userTaskRepository;

    public AllUsersService(IAllUsersRepository repository, IUserTaskRepository userTaskRepository) {
        this.repository = repository;
        this.userTaskRepository = userTaskRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void addUser(User user) {
       repository.save(user);
    }

    @Override
    public User getUserById(Long userId) { return repository.findById(userId).get(); }

    @Override
    public List<UserTask> seeAllTasks(User user) {
        return repository.findUserTasksById(user.getId());
    }

    @Override
    public void addTaskToUser(User user, UserTask newTask) {
        newTask.setUser(user);
        userTaskRepository.save(newTask);
        user.getTasks().add(newTask);
        repository.save(user);
    }

    @Override
    public boolean updateTaskStatus(User user, Long taskId, boolean b) {
        List<UserTask> tasks = user.getTasks();
        UserTask taskToUpdate = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
        if (taskToUpdate == null) {
            return false;
        }
        taskToUpdate.setState(b);
        userTaskRepository.save(taskToUpdate);
        return true;
    }

    @Override
    public boolean deleteTaskFromUser(User user, Long taskId) {
        List<UserTask> tasks = user.getTasks();
        UserTask taskToRemove = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElse(null);
        if (taskToRemove == null) {
            return false;
        }
        tasks.remove(taskToRemove);
        userTaskRepository.delete(taskToRemove);
        repository.save(user);
        return true;
    }

    @Override
    public void deleteUserById(Long id) {
        User user = repository.findById(id).orElse(null);
        repository.delete(user);
    }

}
