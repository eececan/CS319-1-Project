package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.controller.IndividualTourController;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.service.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

import static java.lang.Long.parseLong;

@Controller
public class UIDashBoardController {

    private final EventService eventService;
    private final AllUsersService allUserService;
    private final AuthService authService;
    private final GuideService guideService;
    private final IAdvisorService advisorService;
    private final AllUsersService allUsersService;
    private final PointRecordService pointRecordService;
    private final GuideInTrainingService guideInTrainingService;

    @Autowired
    UIDashBoardController(AllUsersService allUserService, AuthService authService, EventService eventService, GuideService guideService, IAdvisorService advisorService, AllUsersService allUsersService, PointRecordService pointRecordService, IndividualTourController individualTourController, GuideInTrainingService guideInTrainingService){
        this.allUserService = allUserService;
        this.authService = authService;
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.allUsersService = allUsersService;
        this.pointRecordService = pointRecordService;
        this.guideInTrainingService = guideInTrainingService;
    }
    public void populateModelWithUserData(Model model, User user) {
        Advisor advisor = advisorService.findAdvisorsByResponsibleDay(java.time.LocalDate.now().getDayOfWeek());
        List<Tour> tours = eventService.getAllTours();
        List<Fair> fairs = eventService.getAllFairs();
        List<UserTask> tasks = allUsersService.seeAllTasks(user);
        List<User> users = allUsersService.getAllUsers();
        List<PointRecord> pointRecords = pointRecordService.findAllRecords();
        int sum = pointRecords.stream().mapToInt(PointRecord::getPoint).sum();
        long upComing = eventService.getUpcomingEventsCount();
        List<Guide> guides = guideService.getAllGuides();
        long guideCount = guides.size();
        model.addAttribute("guideCount", guideCount);
        model.addAttribute("user", user);
        model.addAttribute("advisor", advisor);
        model.addAttribute("tours", tours);
        model.addAttribute("fairs", fairs);
        model.addAttribute("tasks", tasks);
        model.addAttribute("users", users);
        model.addAttribute("sum", sum);
        model.addAttribute("upComing", upComing);
    }
    @GetMapping("/dashboard")
    public String getDashPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        List<Guide> guides = guideService.getAllGuides();
        List<GuideInTraining> trainings = guideInTrainingService.getAllGuideInTrainings();
        for(GuideInTraining guideInTraining : trainings){
            Guide userr = new Guide();
            userr.setFirstName(guideInTraining.getFirstName());
            userr.setLastName(guideInTraining.getLastName());
            userr.setEmail(guideInTraining.getEmail());
            userr.setPhoneNumber(guideInTraining.getPhoneNumber());
            userr.setPassword(guideInTraining.getPassword());
            userr.setStartDate(guideInTraining.getStartDate());
            userr.setSchedule(guideInTraining.getSchedule());
            userr.setEvents(guideInTraining.getEvents());
            userr.setGrade(guideInTraining.getGrade());
            userr.setDepartment(guideInTraining.getDepartment());
            userr.setDescription(guideInTraining.getDescription());
            userr.setTasks(guideInTraining.getTasks());
            guides.add(userr);
        }
        model.addAttribute("guides", guides);
        User user = allUserService.getUserById(Long.parseLong(username)).get();
       populateModelWithUserData(model,user);
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {

            model.addAttribute("roleUser", "Director");
            return "Director-Dashboard"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            model.addAttribute("roleUser", "Advisor");
            return "Advisor-Dashboard"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            model.addAttribute("roleUser", "Guide");
            Long id = parseLong(username);
            Guide userr = guideService.getGuideById(id);
            Event event = guideService.getUpcomingEventOfGuide(userr);
            long upComing = eventService.getUpcomingEventsCount();
            if (event != null) {
                model.addAttribute("eventDate", event.getDate()); // Assuming event.getDate() returns a LocalDateTime or Date
            } else {
                model.addAttribute("eventDate", new Date()); // No upcoming event
            }
            model.addAttribute("upComing", upComing);
            return "Guide-Dashboard"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            model.addAttribute("roleUser", "Head Secretary");

            return "Head-Secretary-Dashboard"; // Head Secretary's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            model.addAttribute("roleUser", "Guide in Training");

            return"Guide-in-training-Dashboard";
        }
        else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_COORDINATOR"))) {
            model.addAttribute("roleUser", "Coordinator");
            return"Coordinator-Dashboard";
        }
        else
            return "page-empty"; // Default page for unrecognized roles
    }
    }

