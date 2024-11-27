package com.project.btoproject.config;

import com.project.btoproject.model.Role;
import com.project.btoproject.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public ApplicationRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                Role user = new Role();
                user.setName("USER");
                roleRepository.save(user);
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role admin = new Role();
                admin.setName("ADMIN");
                roleRepository.save(admin);
            }
        };
    }
}

