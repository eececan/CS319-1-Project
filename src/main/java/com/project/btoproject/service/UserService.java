package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements IUserService {
    private final IAllUsersService allUsersService;
    private final UserRepository userRepository;

    public UserService(IAllUsersService allUsersService, IAllUsersRepository allUsersRepository, UserRepository userRepository) {
        this.allUsersService = allUsersService;
        this.userRepository = userRepository;
    }

    @Override
    public void enterPersonalInformationGuide(UserEntity userEntity, UserGuideDto userGuideDto) {
        Guide guide = new Guide(userGuideDto.getSchedule(), userGuideDto.getDepartment(), userGuideDto.getGrade(), new ArrayList<>(), new ArrayList<>());
        guide.setPhoneNumber(userGuideDto.getPhoneNumber());
        guide.setDescription(userGuideDto.getDescription());
        guide.setEmail(userGuideDto.getEmail());
        guide.setFirstName(userGuideDto.getFirstName());
        guide.setLastName(userGuideDto.getLastName());
        guide.setId(userEntity.getId());
        guide.setPassword(userEntity.getPassword());
        guide.setPicture(userGuideDto.getPicture());
        guide.setStartDate(new Date());
        allUsersService.addUser(guide);
    }

    @Override
    public void enterPersonalInformationGuideInTraining(UserEntity userEntity, UserGuideInTrainingDto userGuideInTrainingDto) {
        GuideInTraining guideInTraining = new GuideInTraining(userGuideInTrainingDto.getSchedule(), userGuideInTrainingDto.getDepartment(), userGuideInTrainingDto.getGrade(), false, new ArrayList<>());
        guideInTraining.setFirstName(userGuideInTrainingDto.getFirstName());
        guideInTraining.setLastName(userGuideInTrainingDto.getLastName());
        guideInTraining.setPhoneNumber(userGuideInTrainingDto.getPhoneNumber());
        guideInTraining.setEmail(userGuideInTrainingDto.getEmail());
        guideInTraining.setPicture(userGuideInTrainingDto.getPicture());
        guideInTraining.setDescription(userGuideInTrainingDto.getDescription());
        guideInTraining.setStartDate(new Date());
        guideInTraining.setId(userEntity.getId());
        guideInTraining.setPassword(userEntity.getPassword());
        allUsersService.addUser(guideInTraining);
    }

    @Override
    public void enterPersonalInformationAdvisor(UserEntity userEntity, UserAdvisorDto userAdvisorDto) {
        Advisor advisor = new Advisor(userAdvisorDto.getDepartment(), userAdvisorDto.getGrade(), userAdvisorDto.getResponsibleDay(), new ArrayList<>(), new ArrayList<>());
        advisor.setFirstName(userAdvisorDto.getFirstName());
        advisor.setLastName(userAdvisorDto.getLastName());
        advisor.setPhoneNumber(userAdvisorDto.getPhoneNumber());
        advisor.setEmail(userAdvisorDto.getEmail());
        advisor.setPicture(userAdvisorDto.getPicture());
        advisor.setDescription(userAdvisorDto.getDescription());
        advisor.setStartDate(new Date());
        advisor.setId(userEntity.getId());
        advisor.setPassword(userEntity.getPassword());
        allUsersService.addUser(advisor);
    }

    @Transactional
    @Override
    public void deleteUserByUsername(Long id) {
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Clearing roles for user ID: " + user.getId());
        user.getRoles().clear();
        userRepository.save(user);

        System.out.println("Deleting user roles for user ID: " + user.getId());
        userRepository.deleteUserRolesByUserId(user.getId());

        System.out.println("Deleting user entity: " + user.getId());
        userRepository.delete(user);

        System.out.println("Deleting user from all_users service: " + user.getUsername());
        allUsersService.deleteUserById(Long.parseLong(user.getUsername()));
    }



}
