
package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.RoleRepository;
import com.project.btoproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.util.*;

import static java.lang.Long.parseLong;

@Controller
@RequestMapping("ui/UserProfile")
public class UIUserProfileController {


    private final AllUsersService allUsersService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final EventService eventService;
    private final AdvisorService advisorService;
    private final GuideService guideService;
    private final PointRecordService pointRecordService;
    private final RoleRepository roleRepository;
    private final ValidationService validationService;

    UIUserProfileController(AllUsersService allUsersService, UserService userService, PasswordEncoder passwordEncoder, AuthService authService, EventService eventService, AdvisorService advisorService, GuideService guideService, PointRecordService pointRecordService, RoleRepository roleRepository, ValidationService validationService) {
        this.allUsersService = allUsersService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.eventService = eventService;
        this.advisorService = advisorService;
        this.guideService = guideService;
        this.pointRecordService = pointRecordService;
        this.roleRepository = roleRepository;
        this.validationService = validationService;
    }

    @GetMapping("/profile")
    public String getProfile(Model model, RedirectAttributes redirectAttributes,
                             @ModelAttribute("successMessage") String successMessage,
                             @ModelAttribute("errorMessage") String errorMessage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        model.addAttribute("showPopUp", "false");
        Optional<User> user = allUsersService.getUserById(Long.parseLong(username));
        if(!user.isPresent()) {
            model.addAttribute("isUser", "false");
        }
        else{
            model.addAttribute("isUser", "true");
        }
        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("user", user.get());

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            model.addAttribute("role", "ROLE_DIRECTOR");
            return "director-profile"; // Director's profile page
        } else  if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
            model.addAttribute("role", "ROLE_COORDINATOR");
            return "director-profile"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "advisor-profile"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            Guide guide = (Guide) user.get();
            List<Event> event = guide.getEvents();
            if (event == null) {
                event = new ArrayList<>(); // Handle null safely
            }
            int sum = 0;
            for (int i = 0; i < event.size(); i++) {
                System.out.println(event.get(i).getId());
                if (event.get(i).getEventType() == EventType.TOUR) {
                    if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                        sum++;
                    }
                }
            }
            model.addAttribute("sum", sum);
            model.addAttribute("guide", guide);
            model.addAttribute("role", "ROLE_GUIDE");
            model.addAttribute("isUser", "true");
            return "guide-profile"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            model.addAttribute("sum", 0);  //should i delete this
            model.addAttribute("role", "ROLE_HEAD_SECRETARY");
            return "guide-profile";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            GuideInTraining guide = (GuideInTraining) user.get();
            List<Event> event = guide.getEvents();
            if (event == null) {
                event = new ArrayList<>(); // Handle null safely
            }
            int sum = 0;
            for (int i = 0; i < event.size(); i++) {
                if (event.get(i).getEventType() == EventType.TOUR) {
                    if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                        sum++;
                    }
                }
            }
            model.addAttribute("role", "ROLE_GUIDE");
            model.addAttribute("guide", guide);
            model.addAttribute("sum", sum);
            model.addAttribute("isUser", "true");
            return "guide-profile";
        } else {
            return "page-empty";
        }
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam Map<String, Object> dtoMap, Model model) {
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
        }

        if (!allUsersService.hasUserWithId(Long.parseLong(username))) {
            String email = dtoMap.get("email").toString();
            if (email == null || email.trim().isEmpty()) {
                return validationService.validateAndReturn(role, dtoMap, model,
                        "Email cannot be empty. Please provide a valid bilkent email!", Long.parseLong(username), 0);
            }
            else if (email != null && !email.isEmpty()) {
                Optional<User> all_user = allUsersService.getUserByEmail(email);
                if (all_user.isPresent()) {
                    // Return the result of validationService to prevent further execution
                    return validationService.validateAndReturn(role, dtoMap, model,
                            "This email is linked to another user! Please enter another email!", Long.parseLong(username), 0);
                }

            }
            if (email == null || !email.contains("bilkent@edu.tr")) {
                return validationService.validateAndReturn(role, dtoMap, model,
                        "The email you use must be a bilkent mail! Please enter your bilkent mail!", Long.parseLong(username), 0);
            }


            UserEntity user = userService.findUserByUsername(Long.parseLong(username)).get();
            userService.addNewUser(dtoMap, role, user);
            model.addAttribute("showPopUp", "false");



            if (role.equals("ROLE_DIRECTOR")) {
                User allUser = allUsersService.getUserById(Long.parseLong(username)).get();
                model.addAttribute("user", allUser);
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(allUser);
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum = 0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum += pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum", sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);


                return "Director-Dashboard";
            } else if (role.equals("ROLE_COORDINATOR")) {

                User allUser = allUsersService.getUserById(Long.parseLong(username)).get();
                model.addAttribute("user", allUser);
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(allUser);
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum = 0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum += pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum", sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Coordinator-Dashboard";// Coordinator's page
            } else if (role.equals("ROLE_ADVISOR")) {

                User allUser = allUsersService.getUserById(Long.parseLong(username)).get();
                model.addAttribute("user", allUser);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(allUser);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum = 0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum += pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum", sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Advisor-Dashboard"; // Advisor's page
            } else if (role.equals("ROLE_GUIDE")) {

                User allUser = allUsersService.getUserById(Long.parseLong(username)).get();
                model.addAttribute("user", allUser);
                Guide guide = (Guide) allUser;
                if(((Guide) allUser).getSchedule() == null){
                    guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
                model.addAttribute("guide", guide);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(allUser);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum = 0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum += pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum", sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Guide-Dashboard"; // Guide's page
            }else if (role.equals("ROLE_GUIDE_IN_TRAINING")) {

                User allUser = allUsersService.getUserById(Long.parseLong(username)).get();
                model.addAttribute("user", allUser);
                Guide guide = (Guide) allUser;
                if(((Guide) allUser).getSchedule() == null){
                    guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
                model.addAttribute("guide", guide);
                // Fetch advisor of the day
                Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
                // Fetch associated tours and fairs
                List<Tour> tours = eventService.getAllTours();
                List<Fair> fairs = eventService.getAllFairs();
                List<UserTask> tasks = allUsersService.seeAllTasks(allUser);
                // Add advisor, tours, and fairs to the model
                List<User> users = allUsersService.getAllUsers();
                List<PointRecord> pointRecords = pointRecordService.findAllRecords();
                int sum = 0;
                for (int i = 0; i < pointRecords.size(); i++) {
                    sum += pointRecords.get(0).getPoint();
                }
                long upComing = eventService.getUpcomingEventsCount();
                model.addAttribute("upComing", upComing);
                model.addAttribute("sum", sum);
                model.addAttribute("users", users);
                model.addAttribute("tasks", tasks);
                model.addAttribute("advisor", advisor);
                model.addAttribute("tours", tours);
                model.addAttribute("fairs", fairs);
                return "Guide-in-training-Dashboard";
            } else if (role.equals("ROLE_HEAD_SECRETARY")) {

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
        else
        {
            User user = allUsersService.getUserById(Long.parseLong(username)).get();
            int sum = 0;
            if(role.equals("ROLE_GUIDE")){
                Guide guide = (Guide) user;
                List<Event> event = guide.getEvents();
                sum = 0;
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
                }
            }
            else if(role.equals("ROLE_GUIDE_IN_TRAINING")){
                GuideInTraining guide = (GuideInTraining) user;
                List<Event> event = guide.getEvents();
                sum = 0;
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
                }
            }
            String email = dtoMap.get("email").toString();
            if (email == null || email.trim().isEmpty()) {
                return validationService.validateAndReturn(role, dtoMap, model,
                        "Email cannot be empty. Please provide a valid bilkent email!", Long.parseLong(username), sum);
            }
            else if (email != null && !email.isEmpty()) {
                Optional<User> all_user = allUsersService.getUserByEmail(email);
                if (all_user.isPresent()) {
                    if(all_user.get().getId() != Long.parseLong(username)){
                        // Return the result of validationService to prevent further execution
                        return validationService.validateAndReturn(role, dtoMap, model,
                                "This email is linked to another user! Please enter another email!", Long.parseLong(username), sum);
                    }
                }
                if (email == null || !email.contains("@")) {
                    return validationService.validateAndReturn(role, dtoMap, model,
                            "The email must contain '@'! Please enter a valid email address.", Long.parseLong(username), sum);
                } else if (!email.contains("bilkent.edu.tr")) {
                    return validationService.validateAndReturn(role, dtoMap, model,
                            "The email you use must be a Bilkent email! Please enter your Bilkent email!", Long.parseLong(username), sum);
                }
            }
            allUsersService.updateProfile(Long.parseLong(username), dtoMap);
            return "redirect:/ui/UserProfile/profile";
        }
    }




    @GetMapping("/changePassword")
    public String changePassword() {
        return "change-password";
    }

    @PostMapping("/changePasswordBackend")
    public String changePasswordBackend(
            @RequestParam("newPassword1") String newPassword1,
            @RequestParam("newPassword2") String newPassword2,
            RedirectAttributes redirectAttributes
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        if (newPassword1.equals(newPassword2)) {
            userService.changePassword(Long.parseLong(username), newPassword1);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
            return "redirect:/ui/UserProfile/profile"; // Redirect to a success page or profile page
        } else {
            // Handle errors (e.g., wrong current password)
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/ui/UserProfile/changePassword"; // Redirect back to the form
        }
    }

    @GetMapping("/getUserProfile")
    public String getUserProfile(
            @RequestParam Long userId,
            @RequestParam(required = false) String successMessage,
            @RequestParam(required = false) String errorMessage,
            Model model) {
        Optional<User> all_user1 = allUsersService.getUserById(userId);
        if (!all_user1.isPresent()) {
            return "redirect:/redirectToUsersPage";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority()) // Get the role name
                    .orElse("ROLE_UNKNOWN");
        }

        // Add success and error messages to the model
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        // Redirect to user's own profile if the username matches userId
        if (Long.parseLong(username) == userId) {
            return "redirect:/ui/UserProfile/profile";
        } else {
            model.addAttribute("userRole", role);
            Optional<UserEntity> user = userService.findUserByUsername(userId);
            User all_user = allUsersService.getUserById(userId).get();

            if (!user.isPresent()) {
                // Redirect to profile if the user is not found
                return "redirect:/ui/UserProfile/profile";
            } else {
                String roleName = user.get().getRoles().stream()
                        .findFirst()
                        .map(Role::getName)
                        .orElse(null);
                model.addAttribute("role", roleName);
                model.addAttribute("user", all_user);
               if(roleName.equals("ROLE_GUIDE")){
                   Guide guide = (Guide) all_user;
                   if(((Guide) all_user).getSchedule() == null){
                       guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                   }
                   model.addAttribute("guide", guide);
               }
                if(roleName.equals("ROLE_GUIDE_IN_TRAINING")){
                    GuideInTraining guide = (GuideInTraining) all_user;
                    if(((GuideInTraining) all_user).getSchedule() == null){
                        guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                    }
                    model.addAttribute("guide", guide);
                }
                return "view-profile";
            }
        }
    }

    @PostMapping("/changeRole")
    public String changeRole(@RequestParam Long userId, @RequestParam String roleName, RedirectAttributes redirectAttributes) {
        Optional<UserEntity> optionalUser = userService.findUserByUsername(userId);

        if (optionalUser.isEmpty()) {
            redirectAttributes.addAttribute("errorMessage", "User not found.");
            return "redirect:/ui/UserProfile/getUserProfile?userId=" + userId;
        }

        UserEntity user = optionalUser.get();
        String currentRole = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse(null);

        if (roleName.equals(currentRole)) {
            redirectAttributes.addAttribute("errorMessage", "The role is already assigned to this user.");
        } else {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            if (optionalRole.isEmpty()) {
                redirectAttributes.addAttribute("errorMessage", "The specified role does not exist.");
            } else {
                Role newRole = optionalRole.get();
                userService.changeRole(userId, newRole);
                redirectAttributes.addAttribute("successMessage", "Role has been successfully changed.");
            }
        }

        // Redirect to getUserProfile with the updated role and userId
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/ui/UserProfile/getUserProfile";
    }

    @PostMapping("/changeResponsibleDay")
    public String changeResponsibleDay(@RequestParam Long userId, @RequestParam String day, RedirectAttributes redirectAttributes) {
        try {
            // Normalize input
            String normalizedDay = day.trim().toUpperCase(Locale.ENGLISH);


            // Log input for debugging
            System.out.println("Received day input: " + normalizedDay);

            // Check if the day is valid
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(normalizedDay);

            Advisor advisor = advisorService.getAdvisorById(userId);

            if (advisor.getResponsibleDay() != null) {
                if (advisorService.getResponsibleDay(userId).equals(dayOfWeek)) {
                    redirectAttributes.addAttribute("errorMessage", "Advisor is already responsible for the day you selected!");
                    return "redirect:/ui/UserProfile/getUserProfile?userId=" + userId;
                }
            }

            if (!allUsersService.responsibleDayAvailable(dayOfWeek.name())) {
                redirectAttributes.addAttribute("errorMessage", "Another advisor is already responsible for this day! Please make the day available first!");
                return "redirect:/ui/UserProfile/getUserProfile?userId=" + userId;
            } else {
                advisorService.setResponsibleDay(userId, dayOfWeek);
                redirectAttributes.addAttribute("successMessage", "The responsible day has been successfully changed to " + dayOfWeek.name() + "!");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("errorMessage", "Invalid day input. Please provide a valid day of the week.");
        }

        return "redirect:/ui/UserProfile/getUserProfile?userId=" + userId;
    }



}