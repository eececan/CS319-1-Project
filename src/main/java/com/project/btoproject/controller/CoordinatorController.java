package com.project.btoproject.controller;

import com.project.btoproject.dto.CoordinatorDto;
import com.project.btoproject.dto.GuideDto;
import com.project.btoproject.mappings.MappingConfig;
import com.project.btoproject.model.Coordinator;
import com.project.btoproject.service.CoordinatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coordinator")
public class CoordinatorController {

    private final CoordinatorService coordinatorService;
    private final MappingConfig mapper;

    public CoordinatorController(CoordinatorService coordinatorService, MappingConfig mapper) {
        this.coordinatorService = coordinatorService;
        this.mapper = mapper;
    }

    @GetMapping("getCoordinator")
    public List<CoordinatorDto> getCoordinator() {
        return mapper.coordinatorsToCoordinatorDtosList(coordinatorService.getAllCoordinators());
    }

    //TODO the rest


}
