package com.project.btoproject.repository;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAllUsersRepository extends JpaRepository<User, Long> {
    List<UserTask> findUserTasksById(Long id);
}
