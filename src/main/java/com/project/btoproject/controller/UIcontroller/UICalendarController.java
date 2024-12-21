package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.model.*;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UICalendarController {

    private final EventService eventService;
    private final AllUsersService allUsersService;

    @Autowired
    public UICalendarController(EventService eventService, AllUsersService allUsersService) {
        this.eventService = eventService;
        this.allUsersService = allUsersService;
    }

    @GetMapping("/calendar")
    public String calendar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String roleUser="";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        User user = allUsersService.getUserById(Long.parseLong(username)).get();
        model.addAttribute("user", user);
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            roleUser ="DIRECTOR";
            model.addAttribute("roleUser", roleUser);
        }
        else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE")) || (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING")) )) {
            roleUser ="GUIDE";
            model.addAttribute("roleUser", roleUser);
        }
        else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            roleUser ="ADVISOR";
            model.addAttribute("roleUser", roleUser);
        }
        else{
            roleUser ="HEAD SECRETARY";
            model.addAttribute("roleUser", roleUser);
        }
        // Return the calendar view, but don't send events directly in the model
        return "app-calendar";  // The calendar page is rendered normally
    }

    // Endpoint to fetch events as JSON
    @GetMapping("/api/events")
    @ResponseBody
    public List<Map<String, Object>> getEvents() {

        List<Event> events = eventService.getAllEvents();

        // Convert events to a format suitable for FullCalendar
        List<Map<String, Object>> eventList = events.stream().map(event -> {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("title", event.getEventType().name());  // Use event type as title
            eventData.put("start", event.getDate());  // Event start date
            eventData.put("end", event.getDate());  // Optional: if you have an end date
            eventData.put("description", event.getVisitorNotes());  // Optional: description

            return eventData;
        }).collect(Collectors.toList());

        return eventList;
    }
}
