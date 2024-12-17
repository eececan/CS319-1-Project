package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserCoordinatorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.Role;
import com.project.btoproject.model.UserEntity;

import java.util.Optional;

public interface IUserService {

    void enterPersonalInformationGuide(UserEntity user, UserGuideDto userGuideDto) ;
    void enterPersonalInformationGuideInTraining(UserEntity user, UserGuideInTrainingDto userGuideInTrainingDto) ;
    void enterPersonalInformationAdvisor(UserEntity user, UserAdvisorDto userAdvisorDto) ;
    void enterPersonalInformationCoordinator(UserEntity user, UserCoordinatorDto userCoordinatorDto) ;
    void deleteUserByUsername(Long id) ;
    void changePassword(Long id, String password) ;
    void forgotPassword(Long id) ;
    void changeRole(Long id, Role role);
    Optional<UserEntity> findUserByUsername(Long id) ;
}
