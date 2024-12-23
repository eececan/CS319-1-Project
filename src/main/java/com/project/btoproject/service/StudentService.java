package com.project.btoproject.service;

import com.project.btoproject.model.School;
import com.project.btoproject.model.Student;
import com.project.btoproject.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student findOrCreateStudent(String name, String email, String phoneNumber, School school) {
        // Check if the student already exists in the database by email, name, and phone number
        Optional<Student> existingStudent = studentRepository.findByNameAndEmailAndPhoneNumber(name, email, phoneNumber);

        if (existingStudent.isPresent()) {
            // Update the school in case the student has updated their information
            Student student = existingStudent.get();
            student.setSchool(school);
            return studentRepository.save(student);
        }

        // If the student doesn't exist, create a new one
        Student newStudent = new Student();
        newStudent.setName(name);
        newStudent.setEmail(email);
        newStudent.setPhoneNumber(phoneNumber);
        newStudent.setSchool(school);

        return studentRepository.save(newStudent); // Save and return the new Student
    }
}

