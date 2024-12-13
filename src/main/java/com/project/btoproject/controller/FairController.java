package com.project.btoproject.controller;

import com.project.btoproject.model.Fair;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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


}
