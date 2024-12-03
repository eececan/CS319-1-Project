package com.project.btoproject.repository;

import com.project.btoproject.model.School;
import com.project.btoproject.model.SchoolCounselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolCounselorRepository extends JpaRepository<SchoolCounselor, Long> {
    Optional<SchoolCounselor> findByEmailAndSchool(String email, School school);
}

