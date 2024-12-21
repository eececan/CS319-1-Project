package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Fair;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UICalendarController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/calendar")
    public String calendar() {
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
