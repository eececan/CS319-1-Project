package com.project.btoproject.controller;

import com.project.btoproject.dto.GuideDto;
import com.project.btoproject.mappings.MappingConfig;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import com.project.btoproject.model.Tour;
import com.project.btoproject.service.GuideService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guide")
public class GuideController {
    private final GuideService guideService;
    private final MappingConfig mapper;

    public GuideController(GuideService _guideService, MappingConfig _mappingConfig) {
        this.guideService = _guideService;
        this.mapper = _mappingConfig;
    }

    @GetMapping("getAllGuides")
    public List<GuideDto> getAllGuides() {
        return mapper.guidesToGuideDtosList(guideService.getAllGuides());
    }

    @GetMapping("/getGuideByName")
    public GuideDto getGuideByName(@RequestParam String firstName, @RequestParam String lastName) {
        return mapper.guideToGuideDto(guideService.getGuideByName(firstName, lastName));
    }

    @GetMapping("/getGuideById")
    public GuideDto getGuideById(@RequestParam Long id) {
        return mapper.guideToGuideDto(guideService.getGuideById(id));
    }

    @GetMapping("/getEventsOfGuide")
    public List<Event> getEventsOfGuide(@RequestParam Long guideId) {
        Guide guide = guideService.getGuideById(guideId);
        return guideService.seeAssignedEvents(guide);
    }

    @PostMapping("setScheduleOfGuide")
    public void setSchedule(@RequestParam Long guide, @RequestParam int position, @RequestParam char status) {
        guideService.setSchedule(guide,position,status);
    }

}
