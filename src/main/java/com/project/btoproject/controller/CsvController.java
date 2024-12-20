package com.project.btoproject.controller;

import com.project.btoproject.model.HighSchoolForStatistics;
import com.project.btoproject.service.CsvService;
import com.project.btoproject.repository.HighSchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private HighSchoolRepository highSchoolRepository;

    // Endpoint to process the CSV and save data to the database
    @GetMapping("/process-file")
    public String processCsvFile() {
        try {
            // Process the CSV file located in resources and save to DB
            csvService.saveCsvDataToDatabase();
            return "redirect:/csv/high-schools"; // Redirect to the page that displays high schools
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // You can create an error page if needed
        }
    }

    // Endpoint to display high school data
    @GetMapping("/high-schools")
    public String displayHighSchools(Model model) {
        // Fetch all high schools from the database
        List<HighSchoolForStatistics> highSchools = highSchoolRepository.findAll();

        // Add the list to the model to be used in Thymeleaf template
        model.addAttribute("highSchools", highSchools);

        // Return the name of the Thymeleaf template to be rendered
        return "high-schools";  // This will refer to the high_schools.html template
    }
}
