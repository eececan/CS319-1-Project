package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Tour;
import com.project.btoproject.model.Fair;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UIEventController {

    private final EventService eventService;

    @Autowired
    public UIEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/getAllEvents")
    public String getAllEvents(Model model) {
        // Fetch both tours and fairs
        List<Tour> tours = eventService.getAllTours();
        List<Fair> fairs = eventService.getAllFairs();

        // Add both lists to the model
        model.addAttribute("tours", tours);
        model.addAttribute("fairs", fairs);

        // Render a single template
        return "project-list"; // Thymeleaf template
    }
}
