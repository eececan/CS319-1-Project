package com.project.btoproject.controller;

import com.project.btoproject.model.Fair;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/fairs")
public class FairController {
    private final EventService eventService;

    @Autowired
    public FairController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Fair>> getAllFairs() {
        List<Fair> fairs = eventService.getAllFairs();
        return new ResponseEntity<>(fairs, HttpStatus.OK);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approveFair(@PathVariable Long id) {
        eventService.approveFair(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<Void> rejectFair(@PathVariable Long id) {
        eventService.rejectFair(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{fairId}/assign-guide")
    public ResponseEntity<String> assignGuideToFair(
            @PathVariable Long fairId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId");
        try {
            // Call the service method to assign the guide
            eventService.assignGuideToFair(fairId, guideId);
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
    @PostMapping("/{fairId}/increase-guide-count")
    public ResponseEntity<String> increaseGuideCount(@PathVariable Long fairId) {
        try {
            eventService.increaseGuideCountFair(fairId);
            return ResponseEntity.ok("Guide count increased successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Could not increase guide count.");
        }
    }

    @PostMapping("/{fairId}/decrease-guide-count")
    public ResponseEntity<String> removeGuideSlot(
            @PathVariable Long fairId,
            @RequestBody Map<String, Integer> request) {

        int guideIndex = request.get("guideIndex");
        eventService.decreaseGuideCount(fairId);
        return ResponseEntity.ok("Guide slot removed successfully");
    }

    @PostMapping("/{fairId}/remove-guide")
    public ResponseEntity<String> removeGuideFromFair(
            @PathVariable Long fairId,
            @RequestBody Map<String, Long> request) {
        Long guideId = request.get("guideId");
        eventService.removeGuideFromFair(fairId, guideId);
        return ResponseEntity.ok("Guide removed successfully");
    }

    @PostMapping("/{fairId}/join")
    public ResponseEntity<String> joinFair(
            @PathVariable Long fairId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId");
        try {
            eventService.assignGuideToFair(fairId, guideId);
            return ResponseEntity.ok("Successfully joined the fair.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again.");
        }
    }

    @PostMapping("/{fairId}/leave")
    public ResponseEntity<String> leaveFair(
            @PathVariable Long fairId,
            @RequestBody Map<String, Long> request) {

        Long guideId = request.get("guideId");
        try {
            eventService.removeGuideFromFair(fairId, guideId);
            return ResponseEntity.ok("Successfully left the fair!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }
}
