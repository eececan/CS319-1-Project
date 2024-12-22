package com.project.btoproject.controller;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.model.School;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TourStatisticsController {

    private final SchoolService schoolService;
    private final EventService eventService;

    @Autowired
    public TourStatisticsController( SchoolService schoolService, EventService eventService) {
        this.schoolService = schoolService;
        this.eventService = eventService;
    }
}
