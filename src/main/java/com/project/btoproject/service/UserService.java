package com.project.btoproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.dto.*;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
import com.project.btoproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.*;

@Service
public class UserService implements IUserService {
    private final IAllUsersService allUsersService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private IUserHelperService userHelperService;


    public UserService(IAllUsersService allUsersService, IAllUsersRepository allUsersRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, IUserHelperService userHelperService) {
        this.allUsersService = allUsersService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userHelperService = userHelperService;
    }

    @Transactional
    @Override
    public void addNewUser(Map<String, Object> dtoMap, String role, UserEntity user) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (role.equals("ROLE_GUIDE")) {
            UserGuideDto userGuideDto = objectMapper.convertValue(dtoMap, UserGuideDto.class);
            userGuideDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationGuide(user, userGuideDto);
        } else if (role.equals("ROLE_ADVISOR")){
            UserAdvisorDto userAdvisorDto = objectMapper.convertValue(dtoMap, UserAdvisorDto.class);
            userAdvisorDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationAdvisor(user, userAdvisorDto);
        } else if (role.equals("ROLE_GUIDE_IN_TRAINING")) {
            UserGuideInTrainingDto userGuideInTrainingDto = objectMapper.convertValue(dtoMap, UserGuideInTrainingDto.class);
            userGuideInTrainingDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationGuideInTraining(user, userGuideInTrainingDto);
        } else if (role.equals("ROLE_COORDINATOR")) {
            UserCoordinatorDto coordinatorDto = objectMapper.convertValue(dtoMap, UserCoordinatorDto.class);
            coordinatorDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationCoordinator(user, coordinatorDto);
        }
        else if (role.equals("ROLE_HEAD_SECRETARY")) {
            UserHeadSecretaryDto headSecretaryDto = objectMapper.convertValue(dtoMap, UserHeadSecretaryDto.class);
            headSecretaryDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationHeadSecretary(user, headSecretaryDto);
        }
        else if (role.equals("ROLE_DIRECTOR")) {
            UserDirectorDto directorDto = objectMapper.convertValue(dtoMap, UserDirectorDto.class);
            directorDto.setStartDate(new Date());
            userHelperService.enterPersonalInformationDirector(user, directorDto);
        }else {
            System.out.println("User has no matching roles.");
        }
    }

    @Override
    public List<UserEntity> getAllUserEntities() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUserByUsername(Long id) {
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getRoles().clear();
        userRepository.save(user);
        userRepository.deleteUserRolesByUserId(user.getId());
        userRepository.delete(user);
        allUsersService.deleteUserById(Long.parseLong(user.getUsername()));
    }

    @Transactional
    @Override
    public void changePassword(Long id, String password) {
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void forgotPassword(Long id) {
        // Implementation if needed
    }

    @Transactional
    @Override
    public void changeRole(Long id, Role role) {
        UserEntity user = userRepository.findByUsername(id.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        allUsersService.changeRole(id, role.getName());
    }

    @Override
    public Optional<UserEntity> findUserByUsername(Long id) {
        return userRepository.findByUsername(id.toString());
    }

}
