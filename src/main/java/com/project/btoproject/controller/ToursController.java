package com.project.btoproject.controller;

import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class ToursController {

    private final EventService tourService;

    @Autowired
    public ToursController(EventService tourService) {
        this.tourService = tourService;
    }

    // Endpoint to get all tours
    @GetMapping("getAllTours")
    public ResponseEntity<List<Tour>> getAllTours() {
        List<Tour> tours = tourService.getAllTours();
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }


}
