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
        user.getTasks().add(newTask);
        repository.save(user);
        newTask.setUser(user);
        userTaskRepository.save(newTask);
    }
}
