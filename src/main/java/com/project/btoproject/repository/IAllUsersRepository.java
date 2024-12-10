package com.project.btoproject.repository;

import com.project.btoproject.model.User;
import com.project.btoproject.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAllUsersRepository extends JpaRepository<User, Long> {
    @Query("SELECT ut FROM UserTask ut WHERE ut.user.id = :id")
    List<UserTask> findUserTasksById(@Param("id") Long id);
}
