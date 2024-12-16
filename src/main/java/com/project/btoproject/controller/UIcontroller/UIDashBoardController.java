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

import java.util.List;

@Controller
public class UIDashBoardController {

    private final EventService eventService;
    private final AllUsersService allUserService;
    private final AuthService authService;
    private final GuideService guideService;
    private final IAdvisorService advisorService;
    private final AllUsersService allUsersService;
    private final PointRecordService pointRecordService;

    @Autowired
    UIDashBoardController(AllUsersService allUserService, AuthService authService, EventService eventService, GuideService guideService, IAdvisorService advisorService, AllUsersService allUsersService, PointRecordService pointRecordService, IndividualTourController individualTourController ){
        this.allUserService = allUserService;
        this.authService = authService;
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
        this.allUsersService = allUsersService;
        this.pointRecordService = pointRecordService;

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
        User user = allUserService.getUserById(Long.parseLong(username));
       populateModelWithUserData(model,user);
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
            return "Director-Dashboard"; // Director's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
            return "Advisor-Dashboard"; // Advisor's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
            return "Guide-Dashboard"; // Guide's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
            return "Head-Secretary-Dashboard"; // Head Secretary's profile page
        } else if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE_IN_TRAINING"))) {
            return"Guide-In-Training-Dashboard";
        }
        else
            return "page-empty"; // Default page for unrecognized roles
    }
    }

