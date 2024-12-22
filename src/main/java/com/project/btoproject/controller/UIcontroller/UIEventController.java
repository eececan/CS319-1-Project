package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.service.AdvisorService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.GuideService;
import com.project.btoproject.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UIEventController {

    private final EventService eventService;
    private final GuideService guideService;
    private final AdvisorService advisorService;
    private final SchoolService schoolService;

    @Autowired
    public UIEventController(EventService eventService, GuideService guideService, AdvisorService advisorService, SchoolService schoolService) {
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.schoolService = schoolService;
    }

    @GetMapping("/getAllEvents")
    public String getAllEvents(Model model) {
        // Fetch both tours and fairs
        List<Tour> tours = eventService.getAllTours();
        List<Fair> fairs = eventService.getAllFairs();
        List<IndividualTour> individualTours = eventService.getAllIndividualTours();
        // Add both lists to the model
        model.addAttribute("individualTours", individualTours);
        model.addAttribute("tours", tours);
        model.addAttribute("fairs", fairs);

        // Render a single template
        return "guide-tables"; // Thymeleaf template
    }

    @GetMapping("/school-events")
    public String getEventsBySchoolId(Model model) {
        Long schoolId = 3L;
        List<Event> events = eventService.findAllEventsBySchoolId(schoolId);
        model.addAttribute("events", events);
        School school = schoolService.findSchoolById(schoolId);
        model.addAttribute("school", school);
        return "events-of-school";
    }

    @GetMapping("/directorCoordinatorTables")
    public String showEventListCoordinator(
            @RequestParam(defaultValue = "0") int fairApplicationsPage,
            @RequestParam(defaultValue = "12") int fairApplicationsSize,
            @RequestParam(defaultValue = "0") int fairsPage,
            @RequestParam(defaultValue = "12") int fairsSize,
            @RequestParam(defaultValue = "0") int tourApplicationsPage,
            @RequestParam(defaultValue = "12") int tourApplicationsSize,
            @RequestParam(defaultValue = "0") int toursPage,
            @RequestParam(defaultValue = "12") int toursSize,
            @RequestParam(defaultValue = "0") int individualTourApplicationsPage,
            @RequestParam(defaultValue = "12") int individualTourApplicationsSize,
            @RequestParam(defaultValue = "0") int individualToursPage,
            @RequestParam(defaultValue = "12") int individualToursSize,
            @RequestParam(required = false) Integer dayFilter,
            @RequestParam(required = false) String search,
            Model model) {

        try {
            List<Guide> guides = guideService.getAllGuides();
            model.addAttribute("guides", guides);
            Page<Tour> tourApplicationsPageable;
            Page<Tour> toursPageable;
            Page<Fair> fairsPageable;
            Page<Fair> fairApplicationsPageable;
            Page<IndividualTour> individualTourApplicationsPageable;
            Page<IndividualTour> individualToursPageable;
            //If search bar is empty
            if (search != null && !search.trim().isEmpty()) {
                if (dayFilter != null) {
                    // Both search and day filter
                    tourApplicationsPageable = eventService.searchTourApplicationsByDay(search, dayFilter, tourApplicationsPage, tourApplicationsSize);
                    toursPageable = eventService.searchToursByDay(search, dayFilter, toursPage, toursSize);
                    fairsPageable = eventService.searchFairsByDay(search, dayFilter, fairsPage, fairsSize);
                    fairApplicationsPageable = eventService.searchFairApplicationsByDay(search, dayFilter, fairApplicationsPage, fairsSize);
                    individualTourApplicationsPageable = eventService.searchIndividualTourApplicationsByDay(search, dayFilter, individualTourApplicationsPage, individualTourApplicationsSize);
                    individualToursPageable = eventService.searchIndividualToursByDay(search, dayFilter, individualToursPage, individualToursSize);
                } else {
                    // Only search
                    fairApplicationsPageable= eventService.searchFairApplications(search,fairApplicationsPage,fairApplicationsSize);
                    tourApplicationsPageable = eventService.searchTourApplications(search, tourApplicationsPage, tourApplicationsSize);
                    toursPageable = eventService.searchTours(search, toursPage, toursSize);
                    fairsPageable = eventService.searchFairs(search, fairsPage, fairsSize);
                    individualTourApplicationsPageable = eventService.searchIndividualTourApplications(search, individualTourApplicationsPage, individualTourApplicationsSize);
                    individualToursPageable = eventService.searchIndividualTours(search, individualToursPage, individualToursSize);
                }

            }
            //Search Bar is not empty and Filter applied
            else if (dayFilter != null) {
                // Only day filter
                fairApplicationsPageable= eventService.getFairApplicationsByDayPageable(fairApplicationsPage,fairApplicationsSize,dayFilter);
                tourApplicationsPageable = eventService.getTourApplicationsByDayPageable(tourApplicationsPage, tourApplicationsSize, dayFilter);
                toursPageable = eventService.getToursByDayPageable(toursPage, toursSize, dayFilter);
                fairsPageable = eventService.getFairsByDayPageable(fairsPage, fairsSize, dayFilter);
                individualTourApplicationsPageable = eventService.getIndividualTourApplicationsByDayPageable(individualTourApplicationsPage, individualTourApplicationsSize, dayFilter);
                individualToursPageable = eventService.getIndividualToursByDayPageable(individualToursPage, individualToursSize, dayFilter);
            } else {
                // No filters
                fairApplicationsPageable= eventService.getFairApplicationsPageable(fairApplicationsPage,fairApplicationsSize);
                tourApplicationsPageable = eventService.getTourApplicationsPageable(tourApplicationsPage, tourApplicationsSize);
                toursPageable = eventService.getToursPageable(toursPage, toursSize);
                fairsPageable = eventService.getFairsPageable(fairsPage, fairsSize);
                individualTourApplicationsPageable = eventService.getIndividualTourApplicationsPageable(individualTourApplicationsPage, individualTourApplicationsSize);
                individualToursPageable = eventService.getIndividualToursPageable(individualToursPage, individualToursSize);
            }

            List<Fair> fairs = eventService.getFairs();
            // Create guideCounts map for fairs
            Map<Long, List<Integer>> guideCounts = new HashMap<>();
            for (Fair fair : fairs) {
                List<Integer> counts = IntStream.rangeClosed(1, fair.getGuideCount())
                        .boxed()
                        .collect(Collectors.toList());
                guideCounts.put(fair.getId(), counts);
            }
            model.addAttribute("guideCounts", guideCounts);
            model.addAttribute("individualTourApplications", individualTourApplicationsPageable);
            model.addAttribute("individualTours", individualToursPageable);
            model.addAttribute("individualTourApplicationsCurrentPage", individualTourApplicationsPage);
            model.addAttribute("individualTourApplicationsTotalPages", individualTourApplicationsPageable.getTotalPages());
            model.addAttribute("individualToursCurrentPage", individualToursPage);
            model.addAttribute("individualToursTotalPages", individualToursPageable.getTotalPages());
            model.addAttribute("tourApplications", tourApplicationsPageable);
            model.addAttribute("tours", toursPageable);
            model.addAttribute("tourApplicationsCurrentPage", tourApplicationsPage);
            model.addAttribute("tourApplicationsTotalPages", tourApplicationsPageable.getTotalPages());
            model.addAttribute("toursCurrentPage", toursPage);
            model.addAttribute("toursTotalPages", toursPageable.getTotalPages());
            model.addAttribute("fairApplications", fairApplicationsPageable);
            model.addAttribute("fairs", fairsPageable);
            model.addAttribute("fairApplicationsCurrentPage", fairApplicationsPage);
            model.addAttribute("fairApplicationsTotalPages", fairApplicationsPageable.getTotalPages());
            model.addAttribute("fairsCurrentPage", fairsPage);
            model.addAttribute("fairsTotalPages", fairsPageable.getTotalPages());
            model.addAttribute("searchTerm", search);
            model.addAttribute("selectedDay", dayFilter);
            model.addAttribute("dayFilter", dayFilter);
            // -----Individual Tours-----

            return "director-coordinator-tables";  // Match template name exactly
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            throw e;  // Rethrow to see error in logs
        }
    }
    @GetMapping("/headSecretaryTables")
    public String showEventListHeadSecretary(
            @RequestParam(defaultValue = "0") int tourApplicationsPage,
            @RequestParam(defaultValue = "12") int tourApplicationsSize,
            @RequestParam(defaultValue = "0") int toursPage,
            @RequestParam(defaultValue = "12") int toursSize,
            @RequestParam(defaultValue = "0") int individualToursPage,
            @RequestParam(defaultValue = "12") int individualToursSize,
            @RequestParam(defaultValue = "0") int fairsPage,
            @RequestParam(defaultValue = "12") int fairsSize,
            @RequestParam(required = false) Integer dayFilter,
            @RequestParam(required = false) String search,
            Model model) {

        try {

            Page<Tour> tourApplicationsPageable;
            Page<Tour> toursPageable;
            Page<IndividualTour> individualToursPageable;
            Page<Fair> fairsPageable;
            if (search != null && !search.trim().isEmpty()) {
                if (dayFilter != null) {
                    // Both search and day filter
                    tourApplicationsPageable = eventService.searchTourApplicationsByDay(search, dayFilter, tourApplicationsPage, tourApplicationsSize);
                    toursPageable = eventService.searchToursByDay(search, dayFilter, toursPage, toursSize);
                    fairsPageable = eventService.searchFairsByDay(search, dayFilter, fairsPage, fairsSize);
                    individualToursPageable = eventService.searchIndividualToursByDay(search, dayFilter, individualToursPage, individualToursSize);
                } else {
                    // Only search
                    tourApplicationsPageable = eventService.searchTourApplications(search, tourApplicationsPage, tourApplicationsSize);
                    toursPageable = eventService.searchTours(search, toursPage, toursSize);
                    fairsPageable = eventService.searchFairs(search, fairsPage, fairsSize);
                    individualToursPageable = eventService.searchIndividualTours(search, individualToursPage, individualToursSize);
                }
            } else if (dayFilter != null) {
                // Only day filter
                tourApplicationsPageable = eventService.getTourApplicationsByDayPageable(tourApplicationsPage, tourApplicationsSize, dayFilter);
                toursPageable = eventService.getToursByDayPageable(toursPage, toursSize, dayFilter);
                fairsPageable = eventService.getFairsByDayPageable(fairsPage, fairsSize, dayFilter);
                individualToursPageable = eventService.getIndividualToursByDayPageable(individualToursPage, individualToursSize, dayFilter);
            } else {
                // No filters
                tourApplicationsPageable = eventService.getTourApplicationsPageable(tourApplicationsPage, tourApplicationsSize);
                toursPageable = eventService.getToursPageable(toursPage, toursSize);
                fairsPageable = eventService.getFairsPageable(fairsPage, fairsSize);
                individualToursPageable = eventService.getIndividualToursPageable(individualToursPage, individualToursSize);
            }


            model.addAttribute("fairs", fairsPageable);
            model.addAttribute("fairsCurrentPage", fairsPage);
            model.addAttribute("fairsTotalPages", fairsPageable.getTotalPages());
            model.addAttribute("tourApplications", tourApplicationsPageable);
            model.addAttribute("tours", toursPageable);
            model.addAttribute("tourApplicationsCurrentPage", tourApplicationsPage);
            model.addAttribute("tourApplicationsTotalPages", tourApplicationsPageable.getTotalPages());
            model.addAttribute("toursCurrentPage", toursPage);
            model.addAttribute("toursTotalPages", toursPageable.getTotalPages());
            model.addAttribute("individualTours", individualToursPageable);
            model.addAttribute("individualToursCurrentPage", individualToursPage);
            model.addAttribute("individualToursTotalPages", individualToursPageable.getTotalPages());
            model.addAttribute("searchTerm", search);
            model.addAttribute("selectedDay", dayFilter);
            model.addAttribute("dayFilter", dayFilter);
            return "head-secretary-tables";
        } catch (Exception e) {
            e.printStackTrace();  // Log the error
            throw e;  // Rethrow to see error in logs
        }

        /*model.addAttribute("tourApplications", eventService.getTourApplications());
        model.addAttribute("tours", eventService.getTours());
        model.addAttribute("individualTours", eventService.getIndividualTours());
        model.addAttribute("fairs", eventService.getFairs());*/
    }

    @GetMapping("/guideTables")
    public String showEventListGuide(
            @RequestParam(defaultValue = "0") int toursPage,
            @RequestParam(defaultValue = "12") int toursSize,
            @RequestParam(defaultValue = "0") int individualToursPage,
            @RequestParam(defaultValue = "12") int individualToursSize,
            @RequestParam(defaultValue = "0") int fairsPage,
            @RequestParam(defaultValue = "12") int fairsSize,
            @RequestParam(required = false) Integer dayFilter,
            @RequestParam(required = false) String search,
            Model model) {

        try{// ----- Active Guide -----
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserEntity user = new UserEntity();
            Object principal = authentication.getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            String userId = userDetails.getUsername();
            long guideId = Long.parseLong(userId);
            Guide currentGuide = guideService.getGuideById(guideId);
            model.addAttribute("guide", currentGuide);
            // ----- Active Guide -----



            // ----- Paginated Data Fetch -----
            Page<Tour> toursPageable;
            Page<IndividualTour> individualToursPageable;
            Page<Fair> fairsPageable;
            // ----- Paginated Data Fetch -----
            if (search != null && !search.trim().isEmpty()) {
                if (dayFilter != null) {
                    // Both search and day filter
                    toursPageable = eventService.searchToursByDay(search, dayFilter, toursPage, toursSize);
                    fairsPageable = eventService.searchFairsByDay(search, dayFilter, fairsPage, fairsSize);
                    individualToursPageable = eventService.searchIndividualToursByDay(search, dayFilter, individualToursPage, individualToursSize);
                } else {
                    // Only search
                    toursPageable = eventService.searchTours(search, toursPage, toursSize);
                    fairsPageable = eventService.searchFairs(search, fairsPage, fairsSize);
                    individualToursPageable = eventService.searchIndividualTours(search, individualToursPage, individualToursSize);
                }
            } else if (dayFilter != null) {
                // Only day filter
                toursPageable = eventService.getToursByDayPageable(toursPage, toursSize, dayFilter);
                fairsPageable = eventService.getFairsByDayPageable(fairsPage, fairsSize, dayFilter);
                individualToursPageable = eventService.getIndividualToursByDayPageable(individualToursPage, individualToursSize, dayFilter);
            } else {
                // No filters
                toursPageable = eventService.getToursPageable(toursPage, toursSize);
                fairsPageable = eventService.getFairsPageable(fairsPage, fairsSize);
                individualToursPageable = eventService.getIndividualToursPageable(individualToursPage, individualToursSize);
            }
            // ----- Conflict Checks -----
            Map<Long, Boolean> tourConflicts = new HashMap<>();
            for (Tour tour : toursPageable.getContent()) {
                boolean hasConflict = currentGuide.getEvents().stream()
                        .anyMatch(e -> e.getDate().equals(tour.getDate()) &&
                                e instanceof Tour &&
                                ((Tour) e).getHour().equals(tour.getHour()));
                tourConflicts.put(tour.getId(), hasConflict);
            }

            Map<Long, Boolean> individualTourConflicts = new HashMap<>();
            for (IndividualTour individualTour : individualToursPageable.getContent()) {
                boolean hasConflict = currentGuide.getEvents().stream()
                        .anyMatch(e -> e.getDate().equals(individualTour.getDate()) &&
                                e instanceof IndividualTour &&
                                ((IndividualTour) e).getHour().equals(individualTour.getHour()));
                individualTourConflicts.put(individualTour.getId(), hasConflict);
            }

            Map<Long, Boolean> fairConflicts = new HashMap<>();
            for (Fair fair : fairsPageable.getContent()) {
                boolean hasConflict = currentGuide.getEvents().stream()
                        .anyMatch(e -> e.getDate().equals(fair.getDate()) &&
                                e instanceof Fair &&
                                ((Fair) e).getHour().equals(fair.getHour()));
                fairConflicts.put(fair.getId(), hasConflict);
            }
            // ----- Conflict Checks -----

            // ----- Model Attributes -----
            model.addAttribute("tours", toursPageable);
            model.addAttribute("tourConflicts", tourConflicts);
            model.addAttribute("toursCurrentPage", toursPage);
            model.addAttribute("toursTotalPages", toursPageable.getTotalPages());

            model.addAttribute("individualTours", individualToursPageable);
            model.addAttribute("individualTourConflicts", individualTourConflicts);
            model.addAttribute("individualToursCurrentPage", individualToursPage);
            model.addAttribute("individualToursTotalPages", individualToursPageable.getTotalPages());

            model.addAttribute("fairs", fairsPageable);
            model.addAttribute("fairConflicts", fairConflicts);
            model.addAttribute("fairsCurrentPage", fairsPage);
            model.addAttribute("fairsTotalPages", fairsPageable.getTotalPages());
            model.addAttribute("searchTerm", search);
            model.addAttribute("selectedDay", dayFilter);
            model.addAttribute("dayFilter", dayFilter);
            // Get current date as java.util.Date
            Date currentDate = new Date();
            model.addAttribute("currentDate", currentDate);
            // ----- Model Attributes -----

            return "guide-tables";

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            throw e; // Rethrow to see error in logs
        }
    }

    @GetMapping("/advisorTables")
    public String showEventListAdvisor(
            @RequestParam(defaultValue = "0") int tourApplicationsPage,
            @RequestParam(defaultValue = "12") int tourApplicationsSize,
            @RequestParam(defaultValue = "0") int toursPage,
            @RequestParam(defaultValue = "12") int toursSize,
            @RequestParam(defaultValue = "0") int fairsPage,
            @RequestParam(defaultValue = "12") int fairsSize,
            @RequestParam(defaultValue = "0") int individualTourApplicationsPage,
            @RequestParam(defaultValue = "12") int individualTourApplicationsSize,
            @RequestParam(defaultValue = "0") int individualToursPage,
            @RequestParam(defaultValue = "12") int individualToursSize,
            @RequestParam(required = false) Integer dayFilter,
            @RequestParam(required = false) String search,
            Model model) {
    try {
        // Get logged in advisor
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long advisorId = Long.parseLong(userDetails.getUsername());

        Advisor advisor = advisorService.getAdvisorById(advisorId);
        DayOfWeek responsibleDay = advisor.getResponsibleDay();
        List<Guide> guides = guideService.getAllGuides();

        // Fetch paginated data for each tab
        Page<Tour> tourApplicationsPageable;
        Page<Tour> toursPageable;
        Page<Fair> fairsPageable;
        Page<IndividualTour> individualTourApplicationsPageable;
        Page<IndividualTour> individualToursPageable;
        // Combined search and day filter logic
        if (search != null && !search.trim().isEmpty()) {
            if (dayFilter != null) {
                // Both search and day filter
                tourApplicationsPageable = eventService.searchTourApplicationsByDay(search, dayFilter, tourApplicationsPage, tourApplicationsSize);
                toursPageable = eventService.searchToursByDay(search, dayFilter, toursPage, toursSize);
                fairsPageable = eventService.searchFairsByDay(search, dayFilter, fairsPage, fairsSize);
                individualTourApplicationsPageable = eventService.searchIndividualTourApplicationsByDay(search, dayFilter, individualTourApplicationsPage, individualTourApplicationsSize);
                individualToursPageable = eventService.searchIndividualToursByDay(search, dayFilter, individualToursPage, individualToursSize);
            } else {
                // Only search
                tourApplicationsPageable = eventService.searchTourApplications(search, tourApplicationsPage, tourApplicationsSize);
                toursPageable = eventService.searchTours(search, toursPage, toursSize);
                fairsPageable = eventService.searchFairs(search, fairsPage, fairsSize);
                individualTourApplicationsPageable = eventService.searchIndividualTourApplications(search, individualTourApplicationsPage, individualTourApplicationsSize);
                individualToursPageable = eventService.searchIndividualTours(search, individualToursPage, individualToursSize);
            }
        } else if (dayFilter != null) {
            // Only day filter
            tourApplicationsPageable = eventService.getTourApplicationsByDayPageable(tourApplicationsPage, tourApplicationsSize, dayFilter);
            toursPageable = eventService.getToursByDayPageable(toursPage, toursSize, dayFilter);
            fairsPageable = eventService.getFairsByDayPageable(fairsPage, fairsSize, dayFilter);
            individualTourApplicationsPageable = eventService.getIndividualTourApplicationsByDayPageable(individualTourApplicationsPage, individualTourApplicationsSize, dayFilter);
            individualToursPageable = eventService.getIndividualToursByDayPageable(individualToursPage, individualToursSize, dayFilter);
        } else {
            // No filters
            tourApplicationsPageable = eventService.getTourApplicationsPageable(tourApplicationsPage, tourApplicationsSize);
            toursPageable = eventService.getToursPageable(toursPage, toursSize);
            fairsPageable = eventService.getFairsPageable(fairsPage, fairsSize);
            individualTourApplicationsPageable = eventService.getIndividualTourApplicationsPageable(individualTourApplicationsPage, individualTourApplicationsSize);
            individualToursPageable = eventService.getIndividualToursPageable(individualToursPage, individualToursSize);
        }
// Create guide counts map for Tours and Fairs
        Map<Long, List<Integer>> guideCounts = toursPageable.getContent().stream()
                .collect(Collectors.toMap(
                        Tour::getId,
                        tour -> IntStream.rangeClosed(1, tour.getGuideCount())
                                .boxed()
                                .collect(Collectors.toList())
                ));

        Map<Long, List<Integer>> fairGuideCounts = fairsPageable.getContent().stream()
                .collect(Collectors.toMap(
                        Fair::getId,
                        fair -> IntStream.rangeClosed(1, fair.getGuideCount())
                                .boxed()
                                .collect(Collectors.toList())
                ));

        // Add data to the model
        model.addAttribute("responsibleDay", responsibleDay);
        model.addAttribute("guides", guides);
        model.addAttribute("tourApplications", tourApplicationsPageable);
        model.addAttribute("tours", toursPageable);
        model.addAttribute("fairs", fairsPageable);
        model.addAttribute("individualTourApplications", individualTourApplicationsPageable);
        model.addAttribute("individualTours", individualToursPageable);
        model.addAttribute("guideCounts", guideCounts);
        model.addAttribute("fairGuideCounts", fairGuideCounts);
        model.addAttribute("searchTerm", search);
        model.addAttribute("selectedDay", dayFilter);

        // Pagination details for each tab
        model.addAttribute("tourApplicationsCurrentPage", tourApplicationsPage);
        model.addAttribute("tourApplicationsTotalPages", tourApplicationsPageable.getTotalPages());
        model.addAttribute("toursCurrentPage", toursPage);
        model.addAttribute("toursTotalPages", toursPageable.getTotalPages());
        model.addAttribute("fairsCurrentPage", fairsPage);
        model.addAttribute("fairsTotalPages", fairsPageable.getTotalPages());
        model.addAttribute("individualTourApplicationsCurrentPage", individualTourApplicationsPage);
        model.addAttribute("individualTourApplicationsTotalPages", individualTourApplicationsPageable.getTotalPages());
        model.addAttribute("individualToursCurrentPage", individualToursPage);
        model.addAttribute("individualToursTotalPages", individualToursPageable.getTotalPages());
        model.addAttribute("pageSize", tourApplicationsSize);
        model.addAttribute("dayFilter", dayFilter);
        model.addAttribute("selectedDay", dayFilter);


        return "advisor-tables";
    } catch (Exception e) {
        e.printStackTrace(); // Log and rethrow the exception
        throw e;
    }
}


}
