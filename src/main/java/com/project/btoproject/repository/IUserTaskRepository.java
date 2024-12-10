package com.project.btoproject.repository;

import com.project.btoproject.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserTaskRepository extends JpaRepository<UserTask, Integer> {
    UserTask getUserTaskByTaskId(int id);
}
