package com.project.btoproject.service;

import com.project.btoproject.model.User;
import com.project.btoproject.repository.IAllUsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllUsersService implements IAllUsersService {
    private final IAllUsersRepository repository;

    public AllUsersService(IAllUsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void addUser(User user) {
       repository.save(user);
    }
}
