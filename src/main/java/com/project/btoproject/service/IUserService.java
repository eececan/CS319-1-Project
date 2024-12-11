package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.UserEntity;

public interface IUserService {

    void enterPersonalInformationGuide(UserEntity user, UserGuideDto userGuideDto) ;
    void enterPersonalInformationGuideInTraining(UserEntity user, UserGuideInTrainingDto userGuideInTrainingDto) ;
    void enterPersonalInformationAdvisor(UserEntity user, UserAdvisorDto userAdvisorDto) ;
    void deleteUserByUsername(Long id) ;
}
