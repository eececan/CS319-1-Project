package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.dto.*;
import com.project.btoproject.model.*;
import com.project.btoproject.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("ui/auth")
public class UIAuthController {
    private final AuthService authService;
    private final EventService eventService;
    private final GuideService guideService;
    private final IAdvisorService advisorService;
    private final AllUsersService allUsersService;
    private final PointRecordService pointRecordService;

    public UIAuthController(AuthService _authService, EventService eventService, GuideService guideService, IAdvisorService advisorService, AllUsersService allUsersService, PointRecordService pointRecordService) {
        this.authService = _authService;
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.allUsersService = allUsersService;
        this.pointRecordService = pointRecordService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, Model model, HttpServletRequest request) {
        System.out.println(loginDto.getPassword());
        System.out.println(loginDto.getUsername());

        // Pass the request to the AuthService
        ResponseEntity<?> response = authService.login(loginDto, request);

        if (response.getStatusCode().is2xxSuccessful()) {
            AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("auth", auth);

            // Check user roles and return appropriate page
            if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                // Get the current authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = "";
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    username = userDetails.getUsername();
                }
                User user = allUsersService.getUserById(Long.parseLong(username));
                // Add user to the model so that it's accessible in the view
                model.addAttribute("user", user);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(user);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum=0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum+=pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum",sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Director-Dashboard";// Director's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                // Get the current authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = "";
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    username = userDetails.getUsername();
                }
                User user = allUsersService.getUserById(Long.parseLong(username));
                // Add user to the model so that it's accessible in the view
                model.addAttribute("user", user);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(user);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum=0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum+=pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum",sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Advisor-Dashboard"; // Advisor's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                // Get the current authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = "";
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    username = userDetails.getUsername();
                }
                User user = allUsersService.getUserById(Long.parseLong(username));
                // Add user to the model so that it's accessible in the view
                model.addAttribute("user", user);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(user);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum=0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum+=pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum",sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Guide-Dashboard"; // Guide's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
                // Get the current authenticated user
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = "";
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    username = userDetails.getUsername();
                }
                /*User user = allUsersService.getUserById(Long.parseLong(username));
                // Add user to the model so that it's accessible in the view
                model.addAttribute("user", user);*/
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                //List<UserTask> tasks = allUsersService.seeAllTasks(user);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                //List<PointRecord> pointRecords = pointRecordService.getPointRecordsByGuide((Guide) user);
                /*int sum=0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum+=pointRecords.get(0).getPoint();
                }*/
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                //model.addAttribute("sum",sum);
                model.addAttribute("users", users);
                //model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                /*List<User> users = allUsersService.getAllUsers();
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                model.addAttribute("users", users);*/
                return "Head-Secretary-Dashboard"; // Head Secretary's page
            } else {
                return "page-empty"; // Default page for unrecognized roles
            }
        }

        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        return "login";
    }


    @GetMapping("/advisor-tables")
    public String showEventListAdvisor( Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = new UserEntity();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        long advisorId = Long.parseLong(userId);
        Advisor advisor = advisorService.getAdvisorById(advisorId);
        DayOfWeek responsibleDay = advisor.getResponsibleDay();
        List<Guide> guides = guideService.getAllGuides();
        List<Tour> tours = eventService.getTours();
        List<Tour> tourApplications = eventService.getTourApplications();
        // Create guideCounts map
        Map<Long, List<Integer>> guideCounts = tours.stream()
                .collect(Collectors.toMap(
                        Tour::getId,
                        tour -> IntStream.rangeClosed(1, tour.getGuideCount())
                                .boxed()
                                .collect(Collectors.toList())
                ));
        model.addAttribute("tours", tours);
        model.addAttribute("tourApplications", tourApplications);
        model.addAttribute("responsibleDay", responsibleDay);
        model.addAttribute("guides", guides);
        model.addAttribute("guideCounts", guideCounts);
        System.out.println(responsibleDay);
        //model.addAttribute("advisorId", advisorId);
        return "advisor-tables";
    }


    @GetMapping("/head-secretary-tables")
    public String showEventListHeadSecretary(Model model) {

        model.addAttribute("tourApplications", eventService.getTourApplications());
        return "head-secretary-tables";
    }

    @GetMapping("/guide-tables")
    public String showEventListGuide(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = new UserEntity();
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        long guideId = Long.parseLong(userId);
        Guide currentGuide = guideService.getGuideById(guideId);

        // Fetch all tours
        List<Tour> tours = eventService.getTours();

        // Create a map to store tour conflicts
        Map<Long, Boolean> tourConflicts = new HashMap<>();

        // Check conflicts for each tour
        for (Tour tour : tours) {
            boolean hasConflict = currentGuide.getEvents().stream()
                    .anyMatch(e -> e.getDate().equals(tour.getDate()) &&
                            e instanceof Tour &&
                            ((Tour) e).getHour().equals(tour.getHour()));
            tourConflicts.put(tour.getId(), hasConflict);
        }

        // Add attributes to the model
        model.addAttribute("guide", currentGuide);
        model.addAttribute("tours", tours);
        model.addAttribute("tourConflicts", tourConflicts); // Pass conflict information as a separate attribute

        return "guide-tables";
    }

}
