package com.project.tour.controller;
import com.project.tour.entity.TourApplicationEntity;
import com.project.tour.service.TourApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour-applications")
public class TourApplicationController {

    private final TourApplicationService service;

    public TourApplicationController(TourApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TourApplicationEntity> createApplication(@RequestBody TourApplicationEntity application) {
        TourApplicationEntity created = service.createApplication(application);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TourApplicationEntity>> getAllApplications() {
        List<TourApplicationEntity> applications = service.getAllApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourApplicationEntity> getApplicationById(@PathVariable Long id) {
        TourApplicationEntity application = service.getApplicationById(id);
        if (application != null) {
            return ResponseEntity.ok(application);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourApplicationEntity> updateApplication(@PathVariable Long id, @RequestBody TourApplicationEntity applicationDetails) {
        TourApplicationEntity updated = service.updateApplication(id, applicationDetails);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        service.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
