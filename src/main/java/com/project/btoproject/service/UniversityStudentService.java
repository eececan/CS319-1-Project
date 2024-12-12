package com.project.btoproject.service;

import com.project.btoproject.dto.HighSchoolStatisticsDTO;
import com.project.btoproject.model.HighSchoolForStatistics;
import com.project.btoproject.repository.HighSchoolRepository;
import com.project.btoproject.repository.UniversityStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityStudentService {

    private final HighSchoolRepository highSchoolRepository;
    private final UniversityStudentRepository universityStudentRepository;

    @Autowired
    public UniversityStudentService(HighSchoolRepository highSchoolRepository, UniversityStudentRepository universityStudentRepository) {
        this.highSchoolRepository = highSchoolRepository;
        this.universityStudentRepository = universityStudentRepository;
    }

    /**
     * Fetches statistics for all high schools sending students to Bilkent University.
     *
     * @return List of HighSchoolStatisticsDTO containing high school names and student counts
     */
    public List<HighSchoolStatisticsDTO> getHighSchoolStatistics() {
        List<HighSchoolForStatistics> highSchools = highSchoolRepository.findAll(); // Fetch all high schools

        List<HighSchoolStatisticsDTO> statistics = new ArrayList<>();

        for (HighSchoolForStatistics highSchool : highSchools) {
            // Get the student count for this high school
            List<Object[]> results = universityStudentRepository.countStudentsByHighSchool(highSchool);

            for (Object[] result : results) {
                String highSchoolName = (String) result[0];  // High school name
                Long studentCount = (Long) result[1];        // Number of students
                statistics.add(new HighSchoolStatisticsDTO(highSchoolName, studentCount));
            }
        }

        return statistics;
    }
}
