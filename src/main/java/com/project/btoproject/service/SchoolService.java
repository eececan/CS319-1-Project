package com.project.btoproject.service;

import com.project.btoproject.model.School;
import com.project.btoproject.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    /**
     * Finds or creates a School based on the school name.
     *
     * @param schoolName The name of the school.
     * @return A School object.
     */
    public School findOrCreateSchool(String schoolName, String city, String address) {
        // Check if the school already exists in the database
        Optional<School> existingSchool = schoolRepository.findByName(schoolName);

        if (existingSchool.isPresent()) {
            return existingSchool.get();
        }

        // If the school doesn't exist, create a new one
        School newSchool = new School();
        newSchool.setName(schoolName);
        newSchool.setCity(city);
        newSchool.setAddress(address);
        newSchool.setFlag(false);
        return schoolRepository.save(newSchool); // Save and return the new School
    }
}

