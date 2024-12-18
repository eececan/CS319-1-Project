package com.project.btoproject.service;

import com.project.btoproject.dto.*;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UserHelperService implements IUserHelperService {
    private final IAllUsersService allUsersService;


    public UserHelperService(IAllUsersService allUsersService) {
        this.allUsersService = allUsersService;
    }

    @Override
    @Transactional
    public void enterPersonalInformationGuide(UserEntity userEntity, UserGuideDto userGuideDto) {
        Guide guide = new Guide(userGuideDto.getSchedule(), userGuideDto.getDepartment(), userGuideDto.getGrade(), new ArrayList<>(), new ArrayList<>());
        guide.setPhoneNumber(userGuideDto.getPhoneNumber());
        guide.setDescription(userGuideDto.getDescription());
        guide.setEmail(userGuideDto.getEmail());
        guide.setFirstName(userGuideDto.getFirstName());
        guide.setLastName(userGuideDto.getLastName());
        guide.setId(Long.parseLong(userEntity.getUsername()));
        guide.setPassword(userEntity.getPassword());
        guide.setPicture(userGuideDto.getPicture());
        guide.setStartDate(new Date());
        allUsersService.addUser(guide);
    }

    @Override
    @Transactional
    public void enterPersonalInformationGuideInTraining(UserEntity userEntity, UserGuideInTrainingDto userGuideInTrainingDto) {
        GuideInTraining guideInTraining = new GuideInTraining(userGuideInTrainingDto.getSchedule(), userGuideInTrainingDto.getDepartment(), userGuideInTrainingDto.getGrade(), false, new ArrayList<>());
        guideInTraining.setFirstName(userGuideInTrainingDto.getFirstName());
        guideInTraining.setLastName(userGuideInTrainingDto.getLastName());
        guideInTraining.setPhoneNumber(userGuideInTrainingDto.getPhoneNumber());
        guideInTraining.setEmail(userGuideInTrainingDto.getEmail());
        guideInTraining.setPicture(userGuideInTrainingDto.getPicture());
        guideInTraining.setDescription(userGuideInTrainingDto.getDescription());
        guideInTraining.setStartDate(new Date());
        guideInTraining.setId(Long.parseLong(userEntity.getUsername()));
        guideInTraining.setPassword(userEntity.getPassword());
        allUsersService.addUser(guideInTraining);
    }

    @Override
    @Transactional
    public void enterPersonalInformationAdvisor(UserEntity userEntity, UserAdvisorDto userAdvisorDto) {
        Advisor advisor = new Advisor(userAdvisorDto.getDepartment(), userAdvisorDto.getGrade(), userAdvisorDto.getResponsibleDay(), new ArrayList<>(), new ArrayList<>());
        advisor.setFirstName(userAdvisorDto.getFirstName());
        advisor.setLastName(userAdvisorDto.getLastName());
        advisor.setPhoneNumber(userAdvisorDto.getPhoneNumber());
        advisor.setEmail(userAdvisorDto.getEmail());
        advisor.setPicture(userAdvisorDto.getPicture());
        advisor.setDescription(userAdvisorDto.getDescription());
        advisor.setStartDate(new Date());
        advisor.setId(Long.parseLong(userEntity.getUsername()));
        advisor.setPassword(userEntity.getPassword());
        allUsersService.addUser(advisor);
    }

    @Override
    @Transactional
    public void enterPersonalInformationCoordinator(UserEntity userEntity, UserCoordinatorDto userCoordinatorDto) {
        Coordinator coordinator = new Coordinator(userCoordinatorDto.getDepartment(), userCoordinatorDto.getGrade() );
        coordinator.setFirstName(userCoordinatorDto.getFirstName());
        coordinator.setLastName(userCoordinatorDto.getLastName());
        coordinator.setPhoneNumber(userCoordinatorDto.getPhoneNumber());
        coordinator.setEmail(userCoordinatorDto.getEmail());
        coordinator.setPicture(userCoordinatorDto.getPicture());
        coordinator.setDescription(userCoordinatorDto.getDescription());
        coordinator.setStartDate(new Date());
        coordinator.setId(Long.parseLong(userEntity.getUsername()));
        coordinator.setPassword(userEntity.getPassword());
        allUsersService.addUser(coordinator);
    }

    @Override
    @Transactional
    public void enterPersonalInformationHeadSecretary(UserEntity userEntity, UserHeadSecretaryDto userDto) {
        HeadSecretary headSecretary = new HeadSecretary();
        headSecretary.setFirstName(userDto.getFirstName());
        headSecretary.setLastName(userDto.getLastName());
        headSecretary.setPhoneNumber(userDto.getPhoneNumber());
        headSecretary.setEmail(userDto.getEmail());
        headSecretary.setPicture(userDto.getPicture());
        headSecretary.setDescription(userDto.getDescription());
        headSecretary.setStartDate(new Date());
        headSecretary.setId(Long.parseLong(userEntity.getUsername()));
        headSecretary.setPassword(userEntity.getPassword());
        allUsersService.addUser(headSecretary);
    }
}