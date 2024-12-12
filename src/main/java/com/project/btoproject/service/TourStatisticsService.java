package com.project.btoproject.service;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TourStatisticsService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public TourStatisticsService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    // Method to get a list of school names and their counts
    public List<SchoolTourCountDTO> getSchoolTourCounts() {
        List<Object[]> results = schoolRepository.countSchoolsInTours();
        List<SchoolTourCountDTO> tourCounts = new ArrayList<>();

        for (Object[] result : results) {
            String schoolName = (String) result[0];
            Long count = (Long) result[1];
            tourCounts.add(new SchoolTourCountDTO(schoolName, count));
        }

        // Sort the tourCounts list by tourCount in descending order
        tourCounts.sort(Comparator.comparingLong(SchoolTourCountDTO::getTourCount).reversed());

        return tourCounts;
    }
}
