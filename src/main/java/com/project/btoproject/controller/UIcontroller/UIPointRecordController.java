package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.controller.IndividualTourController;
import com.project.btoproject.model.*;
import com.project.btoproject.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UIPointRecordController {
    private final IGuideService guideService;
    private final IGuideInTrainingService guideInTrainingService;
    private final IPointRecordService pointRecordService;
    private final EventService eventService;
    private final IndividualTourController individualTourController;

    public UIPointRecordController(IGuideService guideService, IGuideInTrainingService guideInTrainingService, IPointRecordService pointRecordService, EventService eventService, IndividualTourController individualTourController) {
        this.guideService = guideService;
        this.guideInTrainingService = guideInTrainingService;
        this.eventService = eventService;
        this.pointRecordService = pointRecordService;
        this.individualTourController = individualTourController;
    }

    @GetMapping("/getAllRecords")
    public String getAllPointRecordsPage(Model model) {
        List<PointRecord> records = pointRecordService.findAllRecords().stream().toList();
        model.addAttribute("all_records", records);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
            model.addAttribute("role", role);
        }
        return "all-point-record-list";
    }

    // Get point records page
    @GetMapping("/getRecordsOfGuide")
    public String getPointRecordPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
            model.addAttribute("role", role);
        }
        if(role == "ROLE_GUIDE") {
            Guide guide = guideService.getGuideById(Long.parseLong(username));
            Long guideId = guide.getId();
            List<PointRecord> pointRecords = pointRecordService.getPointRecordsByGuide(guide);
            model.addAttribute("guide_records", pointRecords);
            List<Tour> tours = eventService.getAllTours().stream()
                    .filter(tour -> tour.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());
            List<Fair> fairs = eventService.getAllFairs().stream()
                    .filter(fair -> fair.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());
            List<IndividualTour>individualTours = eventService.getAllIndividualTours().stream()
                    .filter(individualTour -> individualTour.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());

            model.addAttribute("tourMap", tours.stream()
                    .collect(Collectors.toMap(Tour::getId, Tour::getSchool)));
            model.addAttribute("fairMap", fairs.stream()
                    .collect(Collectors.toMap(Fair::getId, fair -> fair.getSchool())));
            model.addAttribute("individualMap", individualTours.stream()
                    .collect(Collectors.toMap(IndividualTour::getId,
                            individual -> individual.getStudent().getSchool())));
            List<Event> events = guide.getEvents();
            model.addAttribute("guide", guide);
            model.addAttribute("events",events);
            return "point-record-list";
        }
        else if(role == "ROLE_GUIDE_IN_TRAINING") {
            GuideInTraining guide = guideInTrainingService.getGuideInTrainingById(Long.parseLong(username));
            Long guideId = guide.getId();
            List<Tour> tours = eventService.getAllTours().stream()
                    .filter(tour -> tour.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());
            List<Fair> fairs = eventService.getAllFairs().stream()
                    .filter(fair -> fair.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());
            List<IndividualTour>individualTours = eventService.getAllIndividualTours().stream()
                    .filter(individualTour -> individualTour.getGuides().stream().anyMatch(guidee -> guide.getId().equals(guideId)))
                    .collect(Collectors.toList());
            model.addAttribute("tourMap", tours.stream()
                    .collect(Collectors.toMap(Tour::getId, Tour::getSchool)));
            model.addAttribute("fairMap", fairs.stream()
                    .collect(Collectors.toMap(Fair::getId, fair -> fair.getSchool())));
            model.addAttribute("individualMap", individualTours.stream()
                    .collect(Collectors.toMap(IndividualTour::getId,
                            individual -> individual.getStudent().getSchool())));

            model.addAttribute("guide", guide);
            return "point-record-list-training";
        }
        else {
            return "point-record-list-training";
        }
    }

}
