package com.project.btoproject.controller;

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
@RequestMapping("/api/tours")
public class TourController {

    private final EventService eventService;

    @Autowired
    public TourController(EventService eventService) {
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

    // Approve tour by Head Secretary
    @PostMapping("/{id}/secretary/approve")
    public ResponseEntity<Void> approveTourBySecretary(@PathVariable Long id) throws InterruptedException {
        // Logic for Head Secretary approval
        System.out.println("Received ID for Approval by Secretary: " + id);
        eventService.approveTourBySecretary(id);
        return ResponseEntity.ok().build();
    }

    // Reject tour by Head Secretary
    @PostMapping("/{id}/secretary/reject")
    public ResponseEntity<Void> rejectTourBySecretary(@PathVariable Long id) throws InterruptedException {
        // Logic for Head Secretary rejection
        System.out.println("Received ID for Rejection by Secretary: " + id);
        eventService.rejectTourBySecretary(id);
        return ResponseEntity.ok().build();
    }

    // Cancel tour by Head Secretary
    @PostMapping("/{id}/secretary/cancel")
    public ResponseEntity<Void> cancelTourBySecretary(@PathVariable Long id) {
        // Logic for Head Secretary cancellation
        System.out.println("Received ID for Cancellation by Secretary: " + id);
        eventService.cancelTourBySecretary(id);
        return ResponseEntity.ok().build();
    }

    // Assign guide to tour
    @PostMapping("/{tourId}/assign-guide")
    public ResponseEntity<String> assignGuideToTour(
            @PathVariable Long tourId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId");
        try {
            // Call the service method to assign the guide
            eventService.assignGuideToTour(tourId, guideId);
            return ResponseEntity.ok("Guide assigned successfully");
        } catch (IllegalArgumentException e) {
            // Handle specific IllegalArgumentException with a meaningful message
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected exceptions with a generic message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/{tourId}/increase-guide-count")
    public ResponseEntity<String> increaseGuideCount(@PathVariable Long tourId) {
        try {
            eventService.increaseGuideCount(tourId);
            return ResponseEntity.ok("Guide count increased successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Could not increase guide count.");
        }
    }

    @PostMapping("/{tourId}/decrease-guide-count")
    public ResponseEntity<String> removeGuideSlot(
            @PathVariable Long tourId,
            @RequestBody Map<String, Integer> request) {

        int guideIndex = request.get("guideIndex"); // Guide index (optional use case)
        eventService.decreaseGuideCount(tourId);
        return ResponseEntity.ok("Guide slot removed successfully");
    }

    @PostMapping("/{tourId}/remove-guide")
    public ResponseEntity<String> removeGuideFromTour(
            @PathVariable Long tourId,
            @RequestBody Map<String, Long> request) {
        Long guideId = request.get("guideId");
        eventService.removeGuideFromTour(tourId, guideId);
        return ResponseEntity.ok("Guide removed successfully");
    }

    @PostMapping("/{tourId}/join")
    public ResponseEntity<String> joinTour(
            @PathVariable Long tourId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId"); // Get guide ID from the request
        try {
            eventService.assignGuideToTour(tourId, guideId); // Call the service method
            return ResponseEntity.ok("Successfully joined the tour.");
        } catch (IllegalArgumentException e) {
            // Handle specific cases, e.g., conflicts, already assigned, etc.
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Generic error handler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping("/{tourId}/leave")
    public ResponseEntity<String> leaveTour(
            @PathVariable Long tourId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId"); // Extract guide ID from the request

        try {
            // Call the service method to remove the guide from the tour
            eventService.removeGuideFromTour(tourId, guideId);
            return ResponseEntity.ok("Successfully left the tour!");
        } catch (IllegalArgumentException e) {
            // Handle specific IllegalArgumentException with a meaningful message
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected exceptions with a generic message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PostMapping("/complete/{tourId}")
    public ResponseEntity<String> markTourAsCompleted(@PathVariable Long tourId) {
        try {
            eventService.markEventAsCompleted(tourId);
            return ResponseEntity.ok("Tour marked as completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
