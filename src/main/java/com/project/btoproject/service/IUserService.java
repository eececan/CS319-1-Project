package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserCoordinatorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Optional;

public interface IUserService {
    void deleteUserByUsername(Long id) ;
    void changePassword(Long id, String password) ;
    void forgotPassword(Long id) ;
    void changeRole(Long id, Role role);
    Optional<UserEntity> findUserByUsername(Long id) ;
    void addNewUser(Map<String, Object> dtoMap, String role, UserEntity user);
}