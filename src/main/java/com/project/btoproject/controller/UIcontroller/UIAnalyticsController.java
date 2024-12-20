package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.enums.SchoolType;
import com.project.btoproject.model.School;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/schools")
public class UIAnalyticsController {

    @Autowired
    private EventService eventService;
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/tour-counts")
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

        // Add data to the model
        model.addAttribute("tourCountDTOs", tourCountDTOs); // Full list for table
        model.addAttribute("topSchools", topSchools);   // Top 4 for the chart
        model.addAttribute("schoolName", schoolName);

        Set<School> allSchools = allTours.stream()
                .filter(tour -> schoolName == null || tour.getSchool().getName().equals(schoolName))
                .map(Tour::getSchool) // Extract the School from the Tour
                .collect(Collectors.toSet());

        // Count the number of Public and Private schools
        long publicSchoolCount = allSchools.stream()
                .filter(school -> SchoolType.STATE == school.getSchoolType())
                .count();
        long privateSchoolCount = allSchools.stream()
                .filter(school -> SchoolType.PRIVATE == school.getSchoolType())
                .count();

        long totalSchools = allSchools.size();
        double privatePercentage = (double) privateSchoolCount / totalSchools * 100;
        double publicPercentage = (double) publicSchoolCount / totalSchools * 100;

        String privateColor = "#303568"; // Example: Custom color for private schools
        String publicColor = "#c00d0a"; // Example: Custom color for public schools

        model.addAttribute("privatePercentage", privatePercentage);
        model.addAttribute("publicPercentage", publicPercentage);
        model.addAttribute("privateColor", privateColor);
        model.addAttribute("publicColor", publicColor);

        Map<String, Long> cityCounts = allSchools.stream()
                .collect(Collectors.groupingBy(School::getCity, Collectors.counting()));

        // Sort cities by the number of schools (descending) and get the top 3 cities
        List<Map.Entry<String, Long>> topCities = cityCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) // Sort by count descending
                .limit(5) // Get the top 3 cities
                .collect(Collectors.toList());

        long totalCities = cityCounts.values().stream().mapToLong(Long::longValue).sum();
        Map<String, Double> cityPercentages = cityCounts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            double percentage = (entry.getValue() * 100.0) / totalCities;
                            // Format the percentage to 1 decimal place
                            return Double.valueOf(String.format("%.1f", percentage));
                        }// Calculate percentage
                ));
        List<String> colors = Arrays.asList(
                "#d0d0d2", "#30356c","#c80706", "#6c6f94"," #78b8c7"
        );

        Map<String, String> cityColors = new HashMap<>();
        int colorIndex = 0;
        for (String city : cityPercentages.keySet()) {
            cityColors.put(city, colors.get(colorIndex % colors.size())); // Cycle through colors if needed
            colorIndex++;
        }

// Prepare the conic-gradient string
        StringBuilder gradientBuilder = new StringBuilder();
        double cumulativePercentage = 0.0;
        for (Map.Entry<String, Double> entry : cityPercentages.entrySet()) {
            String color = cityColors.get(entry.getKey());
            double start = cumulativePercentage;
            cumulativePercentage += entry.getValue();
            gradientBuilder.append(color).append(" ")
                    .append(start).append("% ")
                    .append(cumulativePercentage).append("%, ");
        }

// Remove the trailing comma and space
        String gradientString = gradientBuilder.substring(0, gradientBuilder.length() - 2);

        model.addAttribute("cityGradient", gradientString);
        model.addAttribute("cityColors", cityColors);

        // Prepare data for the view
        model.addAttribute("cityPercentages", cityPercentages);
        model.addAttribute("cityCounts", cityCounts);
        model.addAttribute("totalCities", totalCities);
        model.addAttribute("topCities", topCities);
        return "Analytics"; // Name of the Thymeleaf template
    }
}
