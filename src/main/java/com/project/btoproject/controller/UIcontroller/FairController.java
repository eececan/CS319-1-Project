package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Fair;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FairController {

    private final EventService eventService;

    @Autowired
    public FairController(EventService eventService) {
        this.eventService = eventService;
    }

    // Endpoint to display all fairs
    @GetMapping("/fairs")
    public String getAllFairs(Model model) {
        List<Fair> fairs = eventService.getAllFairs(); // Fetch all fairs from EventService
        model.addAttribute("fairs", fairs); // Add fairs to the model
        return "project-list"; // Render the "project-list.html" Thymeleaf template
    }
}
