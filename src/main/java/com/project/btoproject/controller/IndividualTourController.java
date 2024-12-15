package com.project.btoproject.controller;

import com.project.btoproject.model.Guide;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

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

    @PostMapping("/{id}/assign-guide")
    public ResponseEntity<?> assignGuideToIndividualTour(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        try {
            Long guideId = payload.get("guideId");
            eventService.assignGuideToIndividualTour(id, guideId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Return a clean and structured error message
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @PostMapping("/{id}/remove-guide")
    public ResponseEntity<?> removeGuideFromIndividualTour(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        Long guideId = payload.get("guideId"); // Extract guideId from the request body
        eventService.removeGuideFromIndividualTour(id, guideId); // Call the service method
        return ResponseEntity.ok().build(); // Return success response
    }

}
