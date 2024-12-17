package com.project.btoproject.service;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserCoordinatorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IAllUsersService allUsersService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IAllUsersService allUsersService, IAllUsersRepository allUsersRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.allUsersService = allUsersService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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

    @Transactional
    @Override
    public void changePassword(Long id, String password) {
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        System.out.println("Changed password for user ID: " + user.getId());
    }

    @Override
    public void forgotPassword(Long id) {

    }

    @Transactional
    @Override
    public void changeRole(Long id, Role role){
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findUserByUsername(Long id) {
        return userRepository.findByUsername(id.toString());
    }


}
