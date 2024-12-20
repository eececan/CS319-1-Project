package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.controller.IndividualTourController;
import com.project.btoproject.dto.*;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IGuideInTrainingRepository;
import com.project.btoproject.repository.UserRepository;
import com.project.btoproject.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Calendar;
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
    private final CoordinatorService coordinatorService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final IGuideInTrainingRepository guideInTrainingRepository;

    public UIAuthController(AuthService _authService, EventService eventService, GuideService guideService, IAdvisorService advisorService, AllUsersService allUsersService, PointRecordService pointRecordService, IndividualTourController individualTourController, CoordinatorService coordinatorService, UserRepository userRepository, UserService userService, IGuideInTrainingRepository guideInTrainingRepository) {
        this.authService = _authService;
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.allUsersService = allUsersService;
        this.pointRecordService = pointRecordService;
        this.coordinatorService = coordinatorService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.guideInTrainingRepository = guideInTrainingRepository;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session to log out the user
        request.getSession().invalidate();

        // Clear the SecurityContext
        SecurityContextHolder.clearContext();

        // Redirect to the login page
        return "redirect:/ui/auth/login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("advisorRegisterDto", new AdvisorRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String role,
            @ModelAttribute("registerDto") RegisterDto registerDto,
            @ModelAttribute("advisorRegisterDto") AdvisorRegisterDto advisorRegisterDto,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            if ("ROLE_ADVISOR".equals(role)) {
                if(!allUsersService.responsibleDayAvailable(advisorRegisterDto.getResponsibleDay())){
                    model.addAttribute("errorMessage", "Another advisor is already responsible for this day! Please make the day available first!");
                    return "register";
                }
                else{
                    String response = authService.registerAdvisor(advisorRegisterDto);
                    if (response.equals("User registered successfully!")) {
                        redirectAttributes.addFlashAttribute("successMessage", "Advisor registered successfully!");
                        return "redirect:/getAllUsers";
                    } else {
                        model.addAttribute("errorMessage", response);
                        return "register";
                    }
                }
            } else {
                String response = authService.register(registerDto);
                if (response.equals("User registered successfully!")) {
                    redirectAttributes.addFlashAttribute("successMessage", "User registered successfully!");
                    return "redirect:/getAllUsers";
                } else if (response.equals("Username is already taken!")) {
                    model.addAttribute("errorMessage", "There is an existing user registered with this Bilkent ID!");
                    return "register";
                } else {
                    model.addAttribute("errorMessage", "User could not be registered! Please try again!");
                    return "register";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred: " + e.getMessage());
            return "register";
        }
    }






    @PostMapping("/login")
    public String login(LoginDto loginDto, Model model, HttpServletRequest request) {
        System.out.println(loginDto.getPassword());
        System.out.println(loginDto.getUsername());

        // Pass the request to the AuthService
        ResponseEntity<?> response = authService.login(loginDto, request);

        if (response.getStatusCode().is2xxSuccessful()) {
            AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = "";
            String roleF = "";
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                roleF = userDetails.getAuthorities()
                        .stream()
                        .findFirst()
                        .map(authority -> authority.getAuthority()) // Get the role name
                        .orElse("ROLE_UNKNOWN");
            }
            else{
                return "authentication principal is not in type UserDetails";
            }
            model.addAttribute("auth", authentication);

            if(!allUsersService.hasUserWithId(Long.parseLong(username))){
                model.addAttribute("showPopUp", "true");
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
                    model.addAttribute("sum", 0);
                    HeadSecretary user = new HeadSecretary();
                    user.setId(Long.parseLong(username));
                    model.addAttribute("user", user);
                    model.addAttribute("role", roleF);
                    return "guide-profile";
                }
               if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                   model.addAttribute("role", roleF);
                   Director user = new Director();
                   user.setId(Long.parseLong(username));
                   model.addAttribute("user", user);
                   return "director-profile";
                }

                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
                    model.addAttribute("role", roleF);
                    Coordinator user = new Coordinator();
                    user.setId(Long.parseLong(username));
                    model.addAttribute("user", user);
                    return "director-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                    Advisor user = new Advisor();
                    user.setId(Long.parseLong(username));
                    model.addAttribute("user", user);
                    return "advisor-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                    model.addAttribute("sum", 0);
                    Guide user = new Guide();
                    user.setId(Long.parseLong(username));
                    model.addAttribute("user", user);
                    model.addAttribute("role", roleF);
                    System.out.println("showPopUp: " + model.getAttribute("showPopUp"));
                    return "guide-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
                    model.addAttribute("user", new GuideInTraining());
                    return "guide-in-training-profile";
                }


            }
           else{
                User user = allUsersService.getUserById(Long.parseLong(username));
                model.addAttribute("user", user);
                UserEntity userEntity = userService.findUserByUsername(Long.parseLong(username)).get();
               if(allUsersService.hasMissingInformation(user, userEntity)){
                   model.addAttribute("showPopUp", "true");
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                       model.addAttribute("role", roleF);
                       return "director-profile";
                   }

                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
                       model.addAttribute("role", roleF);
                       return "director-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
                       model.addAttribute("sum", 0);  //should i delete this
                       model.addAttribute("role", "ROLE_HEAD_SECRETARY");
                       return "guide-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                       return "advisor-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                       model.addAttribute("sum", 0);  //should i delete this
                       model.addAttribute("role", "ROLE_GUIDE");
                       return "guide-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
                       model.addAttribute("user", new GuideInTraining());
                       return "guide-in-training-profile";
                   }

               }
                else {
                   model.addAttribute("showPopUp", "false");

                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                       Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                       List<Tour> tours = eventService.getAllTours();
                       List<Fair> fairs = eventService.getAllFairs();
                       List<UserTask> tasks = allUsersService.seeAllTasks(user);
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
                       return "Director-Dashboard";
                   }
                   else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {


                       Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                       List<Tour> tours = eventService.getAllTours();
                       List<Fair> fairs = eventService.getAllFairs();
                       List<UserTask> tasks = allUsersService.seeAllTasks(user);
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
                       return "Coordinator-Dashboard";// Coordinator's page
                   }else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {


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
                   } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {


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
                   }
                   else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {


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
                   }else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {

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
               }
            }

        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        return "login";
    }


    /*@GetMapping("/advisor-tables")
    public String showEventListAdvisor(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Integer dayFilter,
            Model model) {

        // Get logged in advisor
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        long advisorId = Long.parseLong(userDetails.getUsername());

        Advisor advisor = advisorService.getAdvisorById(advisorId);
        DayOfWeek responsibleDay = advisor.getResponsibleDay();
        List<Guide> guides = guideService.getAllGuides();

        // Get all events
        List<Tour> allTours = eventService.getTours();
        List<Tour> tourApplications = eventService.getTourApplications();
        List<Fair> fairs = eventService.getFairs();
        List<IndividualTour> individualTours = eventService.getIndividualTours();
        List<IndividualTour> individualTourApplications = eventService.getIndividualTourApplications();
        Page<Tour> tourApplicationsPageable;
        Page<Tour> toursPageable;
        if (dayFilter != null) {
            tourApplicationsPageable = eventService.getTourApplicationsByDayPageable(page, size, dayFilter);
            toursPageable = eventService.getToursByDayPageable(page, size, dayFilter);


            List<Event> filteredEvents = eventService.getEventsByDay(dayFilter);

            fairs = filteredEvents.stream()
                    .filter(e -> e instanceof Fair)
                    .map(e -> (Fair) e)
                    .collect(Collectors.toList());

            individualTours = filteredEvents.stream()
                    .filter(e -> e instanceof IndividualTour)
                    .map(e -> (IndividualTour) e)
                    .collect(Collectors.toList());

            individualTourApplications = filteredEvents.stream()
                    .filter(e -> e instanceof IndividualTour &&
                            ((IndividualTour)e).getStatus() == Status.NEW_INDIVIDUAL_TOUR_APPLICATION)
                    .map(e -> (IndividualTour) e)
                    .collect(Collectors.toList());
        }
        // Create pagination
        else{
         tourApplicationsPageable = eventService.getTourApplicationsPageable(page, size);
         toursPageable = eventService.getToursPageable(page, size);
        }
        // Create guide counts map
        Map<Long, List<Integer>> guideCounts = allTours.stream()
                .collect(Collectors.toMap(
                        Tour::getId,
                        tour -> IntStream.rangeClosed(1, tour.getGuideCount())
                                .boxed()
                                .collect(Collectors.toList())
                ));

        // Add to model
        model.addAttribute("responsibleDay", responsibleDay);
        model.addAttribute("guides", guides);
        model.addAttribute("fairs", fairs);
        model.addAttribute("guideCounts", guideCounts);
        model.addAttribute("individualTourApplications", individualTourApplications);
        model.addAttribute("individualTours", individualTours);
        model.addAttribute("tourApplications", tourApplicationsPageable);
        model.addAttribute("tours", toursPageable);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tourApplicationsPageable.getTotalPages());
        model.addAttribute("tourTotalPages", toursPageable.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("selectedDay", dayFilter);

        return "advisor-tables";
    }*/

}
