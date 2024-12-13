package com.project.btoproject.controller;

import com.project.btoproject.dto.HighSchoolStatisticsDTO;
import com.project.btoproject.service.UniversityStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class UniversityStudentController {

    private final UniversityStudentService universityStudentService;

    @Autowired
    public UniversityStudentController(UniversityStudentService universityStudentService) {
        this.universityStudentService = universityStudentService;
    }

    /**
     * Endpoint to get statistics of high schools that sent students to Bilkent University.
     *
     * @return List of HighSchoolStatisticsDTO containing high school names and student counts.
     */
    @GetMapping("/bilkent")
    public List<HighSchoolStatisticsDTO> getBilkentStatistics() {
        return universityStudentService.getHighSchoolStatistics();
    }
}
