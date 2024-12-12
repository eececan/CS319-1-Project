package com.project.btoproject.controller;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.service.TourStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TourStatisticsController {

    private final TourStatisticsService tourStatisticsService;

    @Autowired
    public TourStatisticsController(TourStatisticsService tourStatisticsService) {
        this.tourStatisticsService = tourStatisticsService;
    }

    // Endpoint to get schools with their tour counts
    @GetMapping("/schools/tour-counts")
    public List<SchoolTourCountDTO> getSchoolTourCounts() {
        return tourStatisticsService.getSchoolTourCounts();
    }

}
