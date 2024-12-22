package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.controller.IndividualTourController;
import com.project.btoproject.dto.*;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IAllUsersRepository;
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

import static java.lang.Long.parseLong;

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
    private final IAllUsersRepository allUsersRepository;

    public UIAuthController(AuthService _authService, EventService eventService, GuideService guideService, IAdvisorService advisorService, AllUsersService allUsersService, PointRecordService pointRecordService, IndividualTourController individualTourController, CoordinatorService coordinatorService, UserRepository userRepository, UserService userService, IGuideInTrainingRepository guideInTrainingRepository, IAllUsersRepository allUsersRepository) {
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
        this.allUsersRepository = allUsersRepository;
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
    public String login(@ModelAttribute("successMessage") String successMessage,
                        @ModelAttribute("errorMessage") String errorMessage,
                        Model model) {
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);

        // Add messages to the model (if any)
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

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
            @RequestParam(required = false) String role,
            @ModelAttribute("registerDto") RegisterDto registerDto,
            @ModelAttribute("advisorRegisterDto") AdvisorRegisterDto advisorRegisterDto,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
           /*if(registerDto == null){
                model.addAttribute("errorMessage", "Please fill all the required fields");
                return "register";
            }
            if(registerDto.getRole() == null){
                model.addAttribute("errorMessage", "Please select a role!");
                return "register";
            }
            if (registerDto.getUsername() != null && !registerDto.getUsername().isEmpty()) {
                // Check if the username contains only numbers
                if (!registerDto.getUsername().matches("\\d+")) {

                    model.addAttribute("errorMessage", "Username must contain only numbers!");
                    return "register";
                }
            } else {
                model.addAttribute("errorMessage", "Username cannot be empty!");
                return "register";
            }

            if (registerDto.getPassword() != null && !registerDto.getPassword().isEmpty()) {
                // Validate password constraints
                String password = registerDto.getPassword();

                if (password.length() < 8) {
                    model.addAttribute("errorMessage", "Password must be at least 8 characters long!");
                    return "register";
                }

                if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                    model.addAttribute("errorMessage", "Password must contain at least one special character!");
                    return "register";
                }

                if (!password.matches(".*[A-Z].*")) {
                    model.addAttribute("errorMessage", "Password must contain at least one uppercase letter!");
                    return "register";
                }

                if (!password.matches(".*[a-z].*")) {
                    model.addAttribute("errorMessage", "Password must contain at least one lowercase letter!");
                    return "register";
                }

                if (!password.matches(".*\\d.*")) {
                    model.addAttribute("errorMessage", "Password must contain at least one digit!");
                    return "register";
                }
            } else {
                model.addAttribute("errorMessage", "Password cannot be empty!");
                return "register";
            }*/
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
            model.addAttribute("errorMessage", "The user could not be registered! Please try again!");
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

            if(!allUsersService.hasUserWithId(parseLong(username))){
                model.addAttribute("showPopUp", "true");
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
                    model.addAttribute("sum", 0);
                    HeadSecretary user = new HeadSecretary();
                    user.setId(parseLong(username));
                    model.addAttribute("user", user);
                    model.addAttribute("role", roleF);
                    model.addAttribute("isUser", "false");
                    return "guide-profile";
                }
               if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                   model.addAttribute("role", roleF);
                   Director user = new Director();
                   user.setId(parseLong(username));
                   model.addAttribute("user", user);
                   return "director-profile";
                }

                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
                    model.addAttribute("role", roleF);
                    Coordinator user = new Coordinator();
                    user.setId(parseLong(username));
                    model.addAttribute("user", user);
                    return "director-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                    Advisor user = new Advisor();
                    user.setId(parseLong(username));
                    model.addAttribute("user", user);
                    return "advisor-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                    model.addAttribute("sum", 0);
                    Guide user = new Guide();
                    user.setId(parseLong(username));
                    model.addAttribute("user", user);
                    model.addAttribute("role", roleF);
                    model.addAttribute("isUser", "false");
                    return "guide-profile";
                }
                if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
                    GuideInTraining guideInTraining = new GuideInTraining();
                    guideInTraining.setId(parseLong(username));
                    model.addAttribute("user", guideInTraining);
                    model.addAttribute("sum", 0);
                    model.addAttribute("role", roleF);
                    model.addAttribute("isUser", "true");
                    return "guide-profile";
                }


            }
           else{
                User user = allUsersService.getUserById(parseLong(username)).get();
                model.addAttribute("user", user);
                UserEntity userEntity = userService.findUserByUsername(parseLong(username)).get();
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
                       model.addAttribute("sum", 0);
                       model.addAttribute("role", "ROLE_HEAD_SECRETARY");
                       model.addAttribute("isUser", "true");

                       return "guide-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                       return "advisor-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                       Guide guide = (Guide) user;
                       List<Event> event = guide.getEvents();
                       int sum = 0;
                       if(event != null){
                           sum = 0;
                           for (int i = 0; i < event.size(); i++) {
                               System.out.println(event.get(i).getId());
                               if (event.get(i).getEventType() == EventType.TOUR) {
                                   if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                                       sum++;
                                   }
                               }
                           }
                           model.addAttribute("sum", sum);
                       }
                       else {
                           model.addAttribute("sum", 0);
                       }
                       model.addAttribute("role", "ROLE_GUIDE");
                       model.addAttribute("isUser", "true");
                       model.addAttribute("guide", guide);
                       return "guide-profile";
                   }
                   if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
                       GuideInTraining guide = (GuideInTraining) user;
                       List<Event> event = guide.getEvents();
                       int sum = 0;
                       if(event != null){
                           sum = 0;
                           for (int i = 0; i < event.size(); i++) {
                               System.out.println(event.get(i).getId());
                               if (event.get(i).getEventType() == EventType.TOUR) {
                                   if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                                       sum++;
                                   }
                               }
                           }
                           model.addAttribute("sum", sum);
                       }
                       else {
                           model.addAttribute("sum", 0);
                       }
                       model.addAttribute("role", "ROLE_GUIDE_IN_TRAINING");
                       model.addAttribute("isUser", "true");
                       model.addAttribute("guide", guide);
                       return "guide-profile";
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
                       Long id = parseLong(username);
                       Guide userr = guideService.getGuideById(id);
                       Event event = guideService.getUpcomingEventOfGuide(userr);
                       if (event != null) {
                           model.addAttribute("eventDate", event.getDate()); // Assuming event.getDate() returns a LocalDateTime or Date
                       } else {
                           model.addAttribute("eventDate", null); // No upcoming event
                       }
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

    @GetMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { // Basic email validation
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email address.");
            return "redirect:/ui/auth/forgotPasswordPage";
        }
        if(allUsersRepository.findUserByEmail(email) == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "There is no user registered with this email address. Please enter the email address you used in your profile!");
            return "redirect:/ui/auth/forgotPasswordPage";
        }

        try {
            userService.forgotPassword(email);
            redirectAttributes.addFlashAttribute("successMessage", "Your new password has been sent to your email! Please login with your new password!");
            return "redirect:/ui/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while resetting your password. Please try again.");
            return "redirect:/forgot-password.html";
        }
    }

    @GetMapping("/forgotPasswordPage")
    public String forgotPasswordPage(
            @ModelAttribute("errorMessage") String errorMessage,
            @ModelAttribute("successMessage") String successMessage,
            Model model) {
        // Add messages to the model if present
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        return "forgot-password"; // Name of the Thymeleaf template
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
