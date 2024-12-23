package com.project.btoproject.config;

import com.project.btoproject.dto.*;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.HighSchoolRepository;
import com.project.btoproject.repository.IAdvisorRepository;
import com.project.btoproject.repository.IGuideRepository;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.Calendar;
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
    private final HighSchoolRepository highSchoolRepository;
    private final CsvService csvService;

    public DataInitializer(RoleRepository roleRepository, IGuideRepository guideRepository,
                           IAdvisorRepository advisorRepository, AuthService authService,
                           IUserService userService, CoordinatorService coordinatorService, UserHelperService userHelperService, HeadSecretaryService headSecretaryService, IDirectorService directorService, HighSchoolRepository highSchoolRepository, CsvService csvService) {
        this.roleRepository = roleRepository;
        this.guideRepository = guideRepository;
        this.advisorRepository = advisorRepository;
        this.authService = authService;
        this.userService = userService;
        this.coordinatorService = coordinatorService;
        this.userHelperService = userHelperService;
        this.headSecretaryService = headSecretaryService;
        this.directorService = directorService;
        this.highSchoolRepository = highSchoolRepository;
        this.csvService = csvService;
    }

    @Bean
    public ApplicationRunner initializeRoles() {
        return args -> initializeRoleData();
    }

    @PostConstruct
    public void initializeDatabase() throws IOException {
        if (highSchoolRepository.count() == 0) {
            processCsvAndSaveToDatabase();
        }
    }

    private void processCsvAndSaveToDatabase() throws IOException {
        csvService.saveCsvDataToDatabase();
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
    public ApplicationRunner initializeGuides() {
        return args -> initializeGuideData();
    }

  @Bean
    public ApplicationRunner initializeAdvisors() {
        return args -> initializeAdvisorData();
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

    @Bean
    public ApplicationRunner initializeGuideInTrainings() {
        return args -> initializeGuideInTrainingData();
    }

    @Transactional
    public void initializeAdvisorData() {
        if (!roleRepository.findByName("ROLE_ADVISOR").isEmpty()) {
            if (advisorRepository.findByFirstNameAndLastName("Furkan", "Akyol").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200011");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_ADVISOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200011L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserAdvisorDto advisorDto = new UserAdvisorDto();
                advisorDto.setFirstName("Furkan");
                advisorDto.setLastName("Akyol");
                advisorDto.setDescription("Furkan Akyol added as an advisor as an example.");
                advisorDto.setPhoneNumber("0123456789");
                advisorDto.setResponsibleDay(DayOfWeek.MONDAY);
                advisorDto.setEmail("furkan.akyol@ug.bilkent.edu.tr");
                advisorDto.setGrade(3);
                advisorDto.setPicture("picture.jpg");
                advisorDto.setDepartment("CS");
                advisorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
            }
            if (advisorRepository.findByFirstNameAndLastName("Mehmet", "Akyol").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200012");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_ADVISOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200012L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserAdvisorDto advisorDto = new UserAdvisorDto();
                advisorDto.setFirstName("Mehmet");
                advisorDto.setLastName("Akyol");
                advisorDto.setDescription("Mehmet Akyol added as an advisor as an example.");
                advisorDto.setPhoneNumber("0123456789");
                advisorDto.setResponsibleDay(DayOfWeek.TUESDAY);
                advisorDto.setEmail("mehmet.akyol@ug.bilkent.edu.tr");
                advisorDto.setGrade(4);
                advisorDto.setPicture("picture.jpg");
                advisorDto.setDepartment("CS");
                advisorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
            }
            if (advisorRepository.findByFirstNameAndLastName("Ali", "Akyol").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200013");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_ADVISOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200013L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserAdvisorDto advisorDto = new UserAdvisorDto();
                advisorDto.setFirstName("Ali");
                advisorDto.setLastName("Akyol");
                advisorDto.setDescription("Ali Akyol added as an advisor as an example.");
                advisorDto.setPhoneNumber("0123456789");
                advisorDto.setResponsibleDay(DayOfWeek.WEDNESDAY);
                advisorDto.setEmail("ali.akyol@ug.bilkent.edu.tr");
                advisorDto.setGrade(4);
                advisorDto.setPicture("picture.jpg");
                advisorDto.setDepartment("CS");
                advisorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
            }
            if (advisorRepository.findByFirstNameAndLastName("Ömer", "Akyol").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200014");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_ADVISOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200014L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserAdvisorDto advisorDto = new UserAdvisorDto();
                advisorDto.setFirstName("Ömer");
                advisorDto.setLastName("Akyol");
                advisorDto.setDescription("Ömer Akyol added as an advisor as an example.");
                advisorDto.setPhoneNumber("0123456789");
                advisorDto.setResponsibleDay(DayOfWeek.THURSDAY);
                advisorDto.setEmail("omer.akyol@ug.bilkent.edu.tr");
                advisorDto.setGrade(4);
                advisorDto.setPicture("picture.jpg");
                advisorDto.setDepartment("CS");
                advisorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
            }
            if (advisorRepository.findByFirstNameAndLastName("Mert", "Akyol").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200015");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_ADVISOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200015L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserAdvisorDto advisorDto = new UserAdvisorDto();
                advisorDto.setFirstName("Mert");
                advisorDto.setLastName("Akyol");
                advisorDto.setDescription("Mert Akyol added as an advisor as an example.");
                advisorDto.setPhoneNumber("0123456789");
                advisorDto.setResponsibleDay(DayOfWeek.FRIDAY);
                advisorDto.setEmail("mert.akyol@ug.bilkent.edu.tr");
                advisorDto.setGrade(4);
                advisorDto.setPicture("picture.jpg");
                advisorDto.setDepartment("CS");
                advisorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationAdvisor(user, advisorDto);
            }
        }
    }

    @Transactional
    public void initializeGuideData() {
        if (!roleRepository.findByName("ROLE_GUIDE").isEmpty()) {
            if (guideRepository.findByFirstNameAndLastName("Ayca", "Atac").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200001");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200001L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideDto guideDto = new UserGuideDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Ayca");
                guideDto.setLastName("Atac");
                guideDto.setDescription("Ayca Atac added as a guide as an example.");
                guideDto.setPhoneNumber("05370527736");
                guideDto.setEmail("candan.atac@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuide(user, guideDto);
            }
            if (guideRepository.findByFirstNameAndLastName("Emel", "Atac").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200002");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200002L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideDto guideDto = new UserGuideDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Emel");
                guideDto.setLastName("Atac");
                guideDto.setDescription("Emel Atac added as a guide as an example.");
                guideDto.setPhoneNumber("05370527736");
                guideDto.setEmail("emel.atac@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuide(user, guideDto);
            }
            if (guideRepository.findByFirstNameAndLastName("Ece", "Atac").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200003");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200003L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideDto guideDto = new UserGuideDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Ece");
                guideDto.setLastName("Atac");
                guideDto.setDescription("Ece Atac added as a guide as an example.");
                guideDto.setPhoneNumber("05370527736");
                guideDto.setEmail("ece.atac@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuide(user, guideDto);
            }

            if (guideRepository.findByFirstNameAndLastName("Candan", "Atac").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200004");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200004L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideDto guideDto = new UserGuideDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Candan");
                guideDto.setLastName("Atac");
                guideDto.setDescription("Candan Atac added as a guide as an example.");
                guideDto.setPhoneNumber("05370527736");
                guideDto.setEmail("ccandan.atac@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuide(user, guideDto);
            }

            if (guideRepository.findByFirstNameAndLastName("Onur", "Atac").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200005");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200005L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideDto guideDto = new UserGuideDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Onur");
                guideDto.setLastName("Atac");
                guideDto.setDescription("Onur Atac added as a guide as an example.");
                guideDto.setPhoneNumber("05370527736");
                guideDto.setEmail("onur.atac@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuide(user, guideDto);
            }

        }
    }

    @Transactional
    public void initializeGuideInTrainingData() {
        if (!roleRepository.findByName("ROLE_GUIDE_IN_TRAINING").isEmpty()) {
            if (guideRepository.findByFirstNameAndLastName("Ece", "Can").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200021");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200021L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Ece");
                guideDto.setLastName("Can");
                guideDto.setDescription("Ece can added as a guide in training as an example.");
                guideDto.setPhoneNumber("05371127736");
                guideDto.setEmail("ececan@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
            }
            if (guideRepository.findByFirstNameAndLastName("Kadriye", "Can").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200022");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200022L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Kadriye");
                guideDto.setLastName("Can");
                guideDto.setDescription("Kadriye can added as a guide in training as an example.");
                guideDto.setPhoneNumber("05371127736");
                guideDto.setEmail("kadriyecan@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
            }
            if (guideRepository.findByFirstNameAndLastName("Poyraz", "Can").isEmpty()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200023");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200023L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
                guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                guideDto.setFirstName("Poyraz");
                guideDto.setLastName("Can");
                guideDto.setDescription("Poyraz can added as a guide in training as an example.");
                guideDto.setPhoneNumber("05371127736");
                guideDto.setEmail("poyrazcan@ug.bilkent.edu.tr");
                guideDto.setGrade(3);
                guideDto.setPicture("picture.jpg");
                guideDto.setDepartment("CS");
                guideDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
            }
        }
        if (guideRepository.findByFirstNameAndLastName("Tolga", "Can").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("22200024");
            registerDto.setPassword("Password123!!!");
            registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(22200024L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
            guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
            guideDto.setFirstName("Tolga");
            guideDto.setLastName("Can");
            guideDto.setDescription("Tolga Can added as a guide in training as an example.");
            guideDto.setPhoneNumber("05371127736");
            guideDto.setEmail("tolgacan@ug.bilkent.edu.tr");
            guideDto.setGrade(3);
            guideDto.setPicture("picture.jpg");
            guideDto.setDepartment("CS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Set the current date
            calendar.add(Calendar.MONTH, -7); // Subtract 7 months
            Date sevenMonthsAgo = calendar.getTime();
            guideDto.setStartDate(sevenMonthsAgo);

            userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
        }
        if (guideRepository.findByFirstNameAndLastName("Sinem", "Can").isEmpty()) {
            RegisterDto registerDto = new RegisterDto();
            registerDto.setUsername("22200025");
            registerDto.setPassword("Password123!!!");
            registerDto.setRole("ROLE_GUIDE_IN_TRAINING");

            authService.register(registerDto);

            UserEntity user = userService.findUserByUsername(22200025L)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            UserGuideInTrainingDto guideDto = new UserGuideInTrainingDto();
            guideDto.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
            guideDto.setFirstName("Sinem");
            guideDto.setLastName("Can");
            guideDto.setDescription("Sinem Can added as a guide in training as an example.");
            guideDto.setPhoneNumber("05371127736");
            guideDto.setEmail("sinemcan@ug.bilkent.edu.tr");
            guideDto.setGrade(3);
            guideDto.setPicture("picture.jpg");
            guideDto.setDepartment("CS");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date()); // Set the current date
            calendar.add(Calendar.MONTH, -8); // Subtract 7 months
            Date sevenMonthsAgo = calendar.getTime();
            guideDto.setStartDate(sevenMonthsAgo);

            userHelperService.enterPersonalInformationGuideInTraining(user, guideDto);
        }
    }

    @Transactional
    public void initializeCoordinatorData() {
        if (!roleRepository.findByName("ROLE_COORDINATOR").isEmpty()) {
            if (coordinatorService.getCoordinatorById(22200000L) == null) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22200000");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_COORDINATOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22200000L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserCoordinatorDto coordinatorDto = new UserCoordinatorDto();
                coordinatorDto.setFirstName("Boray");
                coordinatorDto.setLastName("Guvenc");
                coordinatorDto.setDescription("Buray Guvenc added as a coordinator as an example.");
                coordinatorDto.setPhoneNumber("0537113327736");
                coordinatorDto.setEmail("buray.guvenc@ug.bilkent.edu.tr");
                coordinatorDto.setGrade(3);
                coordinatorDto.setPicture("picture.jpg");
                coordinatorDto.setDepartment("CS");
                coordinatorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationCoordinator(user, coordinatorDto);
            }
        }
    }

    @Transactional
    public void initializeHeadSecretaryData() {
        if (!roleRepository.findByName("ROLE_HEAD_SECRETARY").isEmpty()) {
            if (!headSecretaryService.getHeadSecretaryById(222L).isPresent()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("222");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_HEAD_SECRETARY");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(222L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserHeadSecretaryDto secretaryDto = new UserHeadSecretaryDto();
                secretaryDto.setFirstName("Dilek");
                secretaryDto.setLastName("Yildiz");
                secretaryDto.setDescription("Dilek Yildiz added as the head secretary as an example.");
                secretaryDto.setPhoneNumber("05371444444");
                secretaryDto.setEmail("dilekyildiz@ug.bilkent.edu.tr");
                secretaryDto.setPicture("dilekpicture.jpg");
                secretaryDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationHeadSecretary(user, secretaryDto);
            }
        }
    }

    @Transactional
    public void initializeDirectorData() {
        if (!roleRepository.findByName("ROLE_DIRECTOR").isEmpty()) {
            if (!directorService.getDirectorById(22L).isPresent()) {
                RegisterDto registerDto = new RegisterDto();
                registerDto.setUsername("22");
                registerDto.setPassword("Password123!!!");
                registerDto.setRole("ROLE_DIRECTOR");

                authService.register(registerDto);

                UserEntity user = userService.findUserByUsername(22L)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                UserDirectorDto directorDto = new UserDirectorDto();
                directorDto.setFirstName("Orsan");
                directorDto.setLastName("Orge");
                directorDto.setDescription("Orsan Orge added as the director as an example.");
                directorDto.setPhoneNumber("0537222222");
                directorDto.setEmail("orsanorge@ug.bilkent.edu.tr");
                directorDto.setPicture("orsanpicture.jpg");
                directorDto.setStartDate(new Date());
                userHelperService.enterPersonalInformationDirector(user, directorDto);
            }
        }
    }




}
