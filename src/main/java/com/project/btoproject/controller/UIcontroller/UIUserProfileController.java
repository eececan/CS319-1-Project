
package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.UserAdvisorDto;
import com.project.btoproject.dto.UserGuideDto;
import com.project.btoproject.dto.UserGuideInTrainingDto;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    UIUserProfileController(AllUsersService allUsersService, UserService userService, PasswordEncoder passwordEncoder, AuthService authService, EventService eventService, AdvisorService advisorService, GuideService guideService, PointRecordService pointRecordService) {
        this.allUsersService = allUsersService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.eventService = eventService;
        this.advisorService = advisorService;
        this.guideService = guideService;
        this.pointRecordService = pointRecordService;
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
        User user = allUsersService.getUserById(Long.parseLong(username));

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("user", user);

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            model.addAttribute("role", "ROLE_DIRECTOR");
            return "director-profile"; // Director's profile page
        } else  if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
            model.addAttribute("role", "ROLE_COORDINATOR");
            return "director-profile"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "advisor-profile"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            Guide guide = (Guide) user;
            List<Event> event = guide.getEvents();

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

            model.addAttribute("role", "ROLE_GUIDE");
            return "guide-profile"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            model.addAttribute("sum", 0);  //should i delete this
            model.addAttribute("role", "ROLE_HEAD_SECRETARY");
            return "guide-profile";
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            GuideInTraining guide = (GuideInTraining) user;
            List<Event> event = guide.getEvents();

            int sum = 0;
            for (int i = 0; i < event.size(); i++) {
                if (event.get(i).getEventType() == EventType.TOUR) {
                    if ((event.get(i).getStatus() == Status.COMPLETED_TOUR)) {
                        sum++;
                    }
                }
            }
            model.addAttribute("sum", sum);
            return "guide-in-training-profile";
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
                    .map(authority -> authority.getAuthority()) // Get the role name
                    .orElse("ROLE_UNKNOWN");
        }
        if (!allUsersService.hasUserWithId(Long.parseLong(username))) {
            UserEntity user = userService.findUserByUsername(Long.parseLong(username)).get();
            userService.addNewUser(dtoMap, role, user);
            model.addAttribute("showPopUp", "false");

            if (role.equals("ROLE_DIRECTOR")) {
                User allUser = allUsersService.getUserById(Long.parseLong(username));
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

                User allUser = allUsersService.getUserById(Long.parseLong(username));
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

                User allUser = allUsersService.getUserById(Long.parseLong(username));
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

                User allUser = allUsersService.getUserById(Long.parseLong(username));
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
                return "Guide-Dashboard"; // Guide's page
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


}
