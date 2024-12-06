package com.project.btoproject.config;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.Role;
import com.project.btoproject.repository.IGuideRepository;
import com.project.btoproject.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final IGuideRepository guideRepository;

    public DataInitializer(RoleRepository _roleRepository, IGuideRepository _guideRepository) {
        this.roleRepository = _roleRepository;
        this.guideRepository = _guideRepository;
    }

    @Bean
    @Transactional
    public ApplicationRunner initializeRoles() {
        return args -> {
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
        };
    }

    @Bean
    @Transactional
    public ApplicationRunner initializeGuides() {
        return args -> {
            if(guideRepository.findByFirstNameAndLastName("Ayca", "Atac").isEmpty()){
                Guide guide = new Guide();
                guide.setStartDate(LocalDate.now());
                guide.setDepartment("CS");
                guide.setEmail("candan.atac@ug.bilkent.edu.tr");
                guide.setFirstName("Ayca");
                guide.setLastName("Atac");
                guide.setSchedule("example schedule");
                guide.setGrade(3);
                guide.setPassword("password");
                guide.setId(22203501L);
                guide.setDescription("Ayca added as a guide for an example.");
                guide.setPhoneNumber("05370527736");
                guideRepository.save(guide);
            }
        };
    }
}
