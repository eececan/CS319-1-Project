package com.project.btoproject.repository;

import com.project.btoproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAllUsersRepository extends JpaRepository<User, Long> {
}
