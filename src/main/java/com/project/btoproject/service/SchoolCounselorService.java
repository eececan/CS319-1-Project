package com.project.btoproject.service;

import com.project.btoproject.model.School;
import com.project.btoproject.model.SchoolCounselor;
import com.project.btoproject.repository.SchoolCounselorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolCounselorService {

    private final SchoolCounselorRepository schoolCounselorRepository;

    @Autowired
    public SchoolCounselorService(SchoolCounselorRepository schoolCounselorRepository) {
        this.schoolCounselorRepository = schoolCounselorRepository;
    }

    /**
     * Finds an existing SchoolCounselor by email or creates a new one if not found.
     * Uses `name` and school as a unique identifier, and ensures the counselor belongs to the given school.
     */
    public SchoolCounselor findOrCreateCounselor(String name, String role, String phoneNumber, String email, School school) {
        return schoolCounselorRepository.findByNameAndSchool(name, school)
                .orElseGet(() -> {
                    // Create a new SchoolCounselor
                    SchoolCounselor newCounselor = new SchoolCounselor();
                    newCounselor.setName(name);
                    newCounselor.setRole(role);
                    newCounselor.setPhoneNumber(phoneNumber);
                    newCounselor.setEmail(email);
                    newCounselor.setSchool(school);
                    newCounselor.setComment("");
                    return schoolCounselorRepository.save(newCounselor);
                });
    }
}
