package com.project.btoproject.service;

import com.project.btoproject.enums.Tier;
import com.project.btoproject.model.School;
import com.project.btoproject.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            School school = existingSchool.get();

            // Update the address only if it's not empty and different from the current address
            if (!address.isEmpty() && !address.equals(school.getAddress())) {
                school.setAddress(address);
                return schoolRepository.save(school); // Save the updated school
            }

            return school; // Return the existing school if no update is needed
        }

        // If the school doesn't exist, create a new one
        School newSchool = new School();
        newSchool.setName(schoolName);
        newSchool.setCity(city);
        newSchool.setAddress(address);
        newSchool.setFlag(false);
        return schoolRepository.save(newSchool); // Save and return the new School
    }

    public void setSchoolTier(School school, Tier tier){
        school.setTier(tier);
        schoolRepository.save(school);
    }
}

