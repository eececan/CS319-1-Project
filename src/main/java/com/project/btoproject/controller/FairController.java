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
@RequestMapping("/api/fairs")
public class FairController {

    private final EventService fairservice;

    @Autowired
    public FairController(EventService tourService) {
        this.fairservice = tourService;
    }

    // Endpoint to get all fairs
    @GetMapping("getAllFairs")
    public ResponseEntity<List<Fair>> getAllFairs() {
        List<Fair> fairs = fairservice.getAllFairs();
        return new ResponseEntity<>(fairs, HttpStatus.OK);
    }


}
