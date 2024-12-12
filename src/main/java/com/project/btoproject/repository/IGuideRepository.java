package com.project.btoproject.repository;

import com.project.btoproject.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IGuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findByFirstNameAndLastName(String firstName, String lastName);
    Guide getGuideById(Long id);
    List<Guide>findAllByDepartment(String department);
}
