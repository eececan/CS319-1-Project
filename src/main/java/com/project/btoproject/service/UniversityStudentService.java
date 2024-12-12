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

    private final UniversityStudentRepository universityStudentRepository;


    @Autowired
    public UniversityStudentService(UniversityStudentRepository universityStudentRepository) {
        this.universityStudentRepository = universityStudentRepository;
    }

    /**
     * Fetches statistics for high schools sending students to Bilkent University.
     *
     * @return List of HighSchoolStatisticsDTO containing high school names and student counts
     */
    public List<HighSchoolStatisticsDTO> getHighSchoolStatistics() {

        List<Object[]> results = universityStudentRepository.countStudentsByHighSchool();
        List<HighSchoolStatisticsDTO> statistics = new ArrayList<>();

        for (Object[] result : results) {
            String highSchoolName = (String) result[0]; // High school name
            Long studentCount = (Long) result[1];       // Number of students
            statistics.add(new HighSchoolStatisticsDTO(highSchoolName, studentCount));
        }

        return statistics;
    }
}
