package com.project.btoproject.repository;

import com.project.btoproject.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find a student by name, email, and phone number
    Optional<Student> findByNameAndEmailAndPhoneNumber(String name, String email, String phoneNumber);

}