package com.project.btoproject.repository;

import com.project.btoproject.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICoordinatorRepository extends JpaRepository<Coordinator, Long> {
    Optional<Coordinator> findByFirstNameAndLastName(String firstName, String lastName);
    Coordinator getCoordinatorById(Long id);
}
