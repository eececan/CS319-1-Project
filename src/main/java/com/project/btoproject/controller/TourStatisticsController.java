package com.project.btoproject.controller;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.model.School;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.SchoolService;
import com.project.btoproject.service.TourStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TourStatisticsController {

    private final TourStatisticsService tourStatisticsService;
    private final SchoolService schoolService;
    private final EventService eventService;

    @Autowired
    public TourStatisticsController(TourStatisticsService tourStatisticsService, SchoolService schoolService, EventService eventService) {
        this.tourStatisticsService = tourStatisticsService;
        this.schoolService = schoolService;
        this.eventService = eventService;
    }

    // Endpoint to get schools with their tour counts
    @GetMapping("/schools/tour-counts")
    public String getSchoolTourCounts(@RequestParam(required = false) String schoolName, Model model) {
        // Fetch the tour counts
        List<Tour> allTours = eventService.getAllTours();

        Map<School, Long> tourCounts = allTours.stream()
                .filter(tour -> schoolName == null || tour.getSchool().getName().equals(schoolName))
                .collect(Collectors.groupingBy(Tour::getSchool, Collectors.counting()));

        // Prepare data for the view
        List<SchoolTourCountDTO> tourCountDTOs = tourCounts.entrySet().stream()
                .map(entry -> new SchoolTourCountDTO(entry.getKey().getName(), entry.getValue()))
                .sorted((a, b) -> b.getTourCount().compareTo(a.getTourCount())) // Sort by tour count descending
                .collect(Collectors.toList());

        // Extract top 4 schools
        List<SchoolTourCountDTO> topSchools = tourCountDTOs.stream().limit(4).collect(Collectors.toList());
        Long maxTourCount = topSchools.get(0).getTourCount();
        System.out.println(maxTourCount);
        // Add data to the model
        model.addAttribute("maxTourCount", maxTourCount);
        model.addAttribute("tourCounts", tourCountDTOs); // Full list for table
        model.addAttribute("topSchools", topSchools);   // Top 4 for the chart
        model.addAttribute("schoolName", schoolName);

        return "tourCountsView"; // Name of the Thymeleaf template
    }

}
