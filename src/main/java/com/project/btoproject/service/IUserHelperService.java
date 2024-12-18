package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserCoordinatorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;


public interface IUserHelperService {
    void enterPersonalInformationGuide(UserEntity user, UserGuideDto userGuideDto) ;
    void enterPersonalInformationGuideInTraining(UserEntity user, UserGuideInTrainingDto userGuideInTrainingDto) ;
    void enterPersonalInformationAdvisor(UserEntity user, UserAdvisorDto userAdvisorDto) ;
    void enterPersonalInformationCoordinator(UserEntity user, UserCoordinatorDto userCoordinatorDto) ;
}