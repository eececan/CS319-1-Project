package com.project.btoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.btoproject.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
