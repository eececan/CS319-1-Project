package com.project.btoproject.controller;

import com.project.btoproject.model.Fair;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventservice;

    @Autowired
    public EventController(EventService eventService) {
        this.eventservice = eventService;
    }

    // Endpoint to get all fairs
    @GetMapping("getAllEvents")
    public ResponseEntity<List<Fair>> getAllFairs() {
        List<Fair> fairs = eventservice.getAllFairs();
        List<Tour> tours = eventservice.getAllTours();
        return new ResponseEntity<>(fairs, HttpStatus.OK);
    }
}
