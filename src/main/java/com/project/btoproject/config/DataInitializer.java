package com.project.btoproject.config;

import com.project.btoproject.dto.*;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAdvisorRepository;
import com.project.btoproject.repository.IGuideRepository;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.service.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Date;

@Configuration
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final IGuideRepository guideRepository;
    private final IAdvisorRepository advisorRepository;
    private final AuthService authService;
    private final IUserService userService;
    private final CoordinatorService coordinatorService;
    private final IUserHelperService userHelperService;
    private final IHeadSecretaryService headSecretaryService;
    private final IDirectorService directorService;

    public DataInitializer(RoleRepository roleRepository, IGuideRepository guideRepository,
                           IAdvisorRepository advisorRepository, AuthService authService,
                           IUserService userService, CoordinatorService coordinatorService, UserHelperService userHelperService, HeadSecretaryService headSecretaryService, IDirectorService directorService) {
        this.roleRepository = roleRepository;
        this.guideRepository = guideRepository;
        this.advisorRepository = advisorRepository;
        this.authService = authService;
        this.userService = userService;
        this.coordinatorService = coordinatorService;
        this.userHelperService = userHelperService;
        this.headSecretaryService = headSecretaryService;
        this.directorService = directorService;
    }

    @Bean
    public ApplicationRunner initializeRoles() {
        return args -> initializeRoleData();
    }

    @Transactional
    public void initializeRoleData() {
        if (roleRepository.findByName("ROLE_GUIDE").isEmpty()) {
            Role user = new Role();
            user.setName("ROLE_GUIDE");
            roleRepository.save(user);
        }
        if (roleRepository.findByName("ROLE_ADVISOR").isEmpty()) {
            Role advisor = new Role();
            advisor.setName("ROLE_ADVISOR");
            roleRepository.save(advisor);
        }
        if (roleRepository.findByName("ROLE_COORDINATOR").isEmpty()) {
            Role coordinator = new Role();
            coordinator.setName("ROLE_COORDINATOR");
            roleRepository.save(coordinator);
        }
        if (roleRepository.findByName("ROLE_HEAD_SECRETARY").isEmpty()) {
            Role headSecretary = new Role();
            headSecretary.setName("ROLE_HEAD_SECRETARY");
            roleRepository.save(headSecretary);
        }
        if (roleRepository.findByName("ROLE_DIRECTOR").isEmpty()) {
            Role director = new Role();
            director.setName("ROLE_DIRECTOR");
            roleRepository.save(director);
        }
        if (roleRepository.findByName("ROLE_GUIDE_IN_TRAINING").isEmpty()) {
            Role guideInTraining = new Role();
            guideInTraining.setName("ROLE_GUIDE_IN_TRAINING");
            roleRepository.save(guideInTraining);
        }
    }

   @Bean
    public ApplicationRunner initializeAdvisors() {
        return args -> initializeAdvisorData();
    }

    @Bean
    public ApplicationRunner initializeGuides() {
        return args -> initializeGuideData();
    }

    @Bean
    public ApplicationRunner initializeGuideInTrainings() {
        return args -> initializeGuideInTrainingData();
    }

    @Bean
    public ApplicationRunner initializeCoordinators() {
        return args -> initializeCoordinatorData();
    }

    @Bean
    public ApplicationRunner initializeHeadSecretary() {
        return args -> initializeHeadSecretaryData();
    }

    @Bean
    public ApplicationRunner initializeDirector() {
        return args -> initializeDirectorData();
    }

    @Transactional
    public void initializeAdvisorData() {
        if (advisorRepository.findByFirstNameAndLastName("Furkan", "Akyol").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("1");
            registerDto.setPassword("1");
            registerDto.setRole("ROLE_ADVISOR");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(1L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserAdvisorDto advisorDto = new UserAdvisorDto();
            advisorDto.setFirstName("Furkan");
            advisorDto.setLastName("Akyol");
            advisorDto.setDescription("Furkan Akyol added as an advisor as an example.");
            advisorDto.setPhoneNumber("0123456789");
            advisorDto.setResponsibleDay(DayOfWeek.WEDNESDAY);
            advisorDto.setEmail("furkan.akyol@ug.bilkent.edu.tr");
            advisorDto.setGrade(3);
            advisorDto.setPicture("picture.jpg");
            advisorDto.setDepartment("CS");
            userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
        }
        if (advisorRepository.findByFirstNameAndLastName("Mehmet", "Akyol").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("8");
            registerDto.setPassword("8");
            registerDto.setRole("ROLE_ADVISOR");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(8L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserAdvisorDto advisorDto = new UserAdvisorDto();
            advisorDto.setFirstName("Mehmet");
            advisorDto.setLastName("Akyol");
            advisorDto.setDescription("Furkan Akyoal added as an advisor as an example.");
            advisorDto.setPhoneNumber("0123456789");
            advisorDto.setResponsibleDay(DayOfWeek.MONDAY);
            advisorDto.setEmail("afurkan.akyol@ug.bilkent.edu.tr");
            advisorDto.setGrade(3);
            advisorDto.setPicture("picture.jpg");
            advisorDto.setDepartment("CS");
            userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
        }
    }

    @Transactional
    public void initializeGuideData() {
        if (guideRepository.findByFirstNameAndLastName("Ayca", "Atac").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("2");
            registerDto.setPassword("2");
            registerDto.setRole("ROLE_GUIDE");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(2L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserGuideDto guideDto = new UserGuideDto();
            guideDto.setSchedule("example guide schedule");
            guideDto.setFirstName("Ayca");
            guideDto.setLastName("Atac");
            guideDto.setDescription("Ayca Atac added as a guide as an example.");
            guideDto.setPhoneNumber("05370527736");
            guideDto.setEmail("candan.atac@ug.bilkent.edu.tr");
            guideDto.setGrade(3);
            guideDto.setPicture("picture.jpg");
            guideDto.setDepartment("CS");
            userHelperService.enterPersonalInformationGuide(user, guideDto);
        }
    }


    @Transactional
    public void initializeGuideInTrainingData() {
        if (guideRepository.findByFirstNameAndLastName("Ece", "Can").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("3");
            registerDto.setPassword("3");
            registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(3L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
            guideDto.setSchedule("example guide in training schedule");
            guideDto.setFirstName("Ece");
            guideDto.setLastName("Can");
            guideDto.setDescription("Ece can added as a guide in training as an example.");
            guideDto.setPhoneNumber("05371127736");
            guideDto.setEmail("ececan@ug.bilkent.edu.tr");
            guideDto.setGrade(3);
            guideDto.setPicture("picture.jpg");
            guideDto.setDepartment("CS");
            userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
        }
    }

    @Transactional
    public void initializeCoordinatorData() {
        if (coordinatorService.getCoordinatorById(4L) == null) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("4");
            registerDto.setPassword("4");
            registerDto.setRole("ROLE_COORDINATOR");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(4L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserCoordinatorDto coordinatorDto = new UserCoordinatorDto();
            coordinatorDto.setFirstName("Ceren");
            coordinatorDto.setLastName("Celik");
            coordinatorDto.setDescription("Ceren celik added as a coordinator as an example.");
            coordinatorDto.setPhoneNumber("0537113327736");
            coordinatorDto.setEmail("ceren.celik@ug.bilkent.edu.tr");
            coordinatorDto.setGrade(3);
            coordinatorDto.setPicture("picture.jpg");
            coordinatorDto.setDepartment("CS");
            userHelperService.enterPersonalInformationCoordinator(user, coordinatorDto);
        }
    }

    @Transactional
    public void initializeHeadSecretaryData() {
        if (!headSecretaryService.getHeadSecretaryById(5L).isPresent()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("5");
            registerDto.setPassword("5");
            registerDto.setRole("ROLE_HEAD_SECRETARY");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(5L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserHeadSecretaryDto secretaryDto = new UserHeadSecretaryDto();
            secretaryDto.setFirstName("Dilek");
            secretaryDto.setLastName("Yildiz");
            secretaryDto.setDescription("Dilek Yildiz added as the head secretary as an example.");
            secretaryDto.setPhoneNumber("05371444444");
            secretaryDto.setEmail("dilekyildiz@ug.bilkent.edu.tr");
            secretaryDto.setPicture("dilekpicture.jpg");
            userHelperService.enterPersonalInformationHeadSecretary(user, secretaryDto);
        }
    }

    @Transactional
    public void initializeDirectorData() {

        if (!directorService.getDirectorById(6L).isPresent()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("6");
            registerDto.setPassword("6");
            registerDto.setRole("ROLE_DIRECTOR");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(6L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserDirectorDto directorDto = new UserDirectorDto();
            directorDto.setFirstName("Orsan");
            directorDto.setLastName("Orge");
            directorDto.setDescription("Orsan Orge added as the director as an example.");
            directorDto.setPhoneNumber("0537222222");
            directorDto.setEmail("orsanorge@ug.bilkent.edu.tr");
            directorDto.setPicture("orsanpicture.jpg");
            userHelperService.enterPersonalInformationDirector(user, directorDto);
        }
    }


}
