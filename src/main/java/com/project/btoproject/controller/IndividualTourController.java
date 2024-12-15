package com.project.btoproject.controller;

import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/individual-tours")
public class IndividualTourController {

    private final EventService eventService;

    @Autowired
    public IndividualTourController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{id}/advisor/approve")
    public ResponseEntity<?> approveIndividualTour(@PathVariable Long id) {
        eventService.approveIndividualTour(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/advisor/reject")
    public ResponseEntity<?> rejectIndividualTour(@PathVariable Long id) {
        eventService.rejectIndividualTour(id); // Call the service method for rejection
        return ResponseEntity.ok().build();
    }
}
