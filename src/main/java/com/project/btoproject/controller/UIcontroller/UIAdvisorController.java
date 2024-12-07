package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/tours")
public class UIAdvisorController {

    private final EventService eventService;

    @Autowired
    public UIAdvisorController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{id}/advisor/approve")
    public ResponseEntity<Void> approveTourByAdvisor(@PathVariable Long id) {
        // Logic for advisor approval
        System.out.println("Received ID for Approval: " + id);
        eventService.approveTourByAdvisor(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/advisor/reject")
    public ResponseEntity<Void> rejectTourByAdvisor(@PathVariable Long id) {
        // Logic for advisor rejection
        eventService.rejectTourByAdvisor(id);
        return ResponseEntity.ok().build();
    }
}
