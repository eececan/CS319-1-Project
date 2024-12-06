package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UITourController {

    private final EventService eventService;

    @Autowired
    public UITourController(EventService eventService) {
        this.eventService = eventService;
    }

    // Endpoint to display all tours
    @GetMapping("/getAllTours")
    public String getAllTours(Model model) {
        List<Tour> tours = eventService.getAllTours(); // Fetch all tours from EventService
        model.addAttribute("tours", tours); // Add tours to the model
        return "project-list"; // Render the "project-list.html" Thymeleaf template
    }
}
