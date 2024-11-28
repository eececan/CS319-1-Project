package com.project.btoproject.config;

import com.project.btoproject.model.Role;
import com.project.btoproject.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository _roleRepository) {
        this.roleRepository = _roleRepository;
    }

    @Bean
    @Transactional
    public ApplicationRunner initializeRoles() {
        return args -> {
            if (roleRepository.findByName("USER").isPresent()) {
                roleRepository.deleteByName("USER");
            }
            if (roleRepository.findByName("ADMIN").isPresent()) {
                roleRepository.deleteByName("ADMIN");
            }
            if (roleRepository.findByName("GUIDE").isEmpty()) {
                Role user = new Role();
                user.setName("GUIDE");
                roleRepository.save(user);
            }
            if (roleRepository.findByName("ADVISOR").isEmpty()) {
                Role advisor = new Role();
                advisor.setName("ADVISOR");
                roleRepository.save(advisor);
            }
            if (roleRepository.findByName("COORDINATOR").isEmpty()) {
                Role coordinator = new Role();
                coordinator.setName("COORDINATOR");
                roleRepository.save(coordinator);
            }
            if (roleRepository.findByName("HEAD_SECRETARY").isEmpty()) {
                Role headSecretary = new Role();
                headSecretary.setName("HEAD_SECRETARY");
                roleRepository.save(headSecretary);
            }
            if (roleRepository.findByName("DIRECTOR").isEmpty()) {
                Role director = new Role();
                director.setName("DIRECTOR");
                roleRepository.save(director);
            }
        };
    }
}
