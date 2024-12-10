package com.project.btoproject.controller.UIcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.btoproject.dto.*;
import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Guide;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.UserEntity;
import com.project.btoproject.service.AuthService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.GuideService;
import com.project.btoproject.service.IAdvisorService;
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

    public UIAuthController(AuthService _authService, EventService eventService, GuideService guideService, IAdvisorService advisorService) {
        this.authService = _authService;
        this.eventService = eventService;
        this.guideService = guideService;
        this.advisorService = advisorService;
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
                return "Director-Dashboard"; // Director's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                return "Advisor-Dashboard"; // Advisor's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                return "Guide-taskboard"; // Guide's page
            } else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
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

        List<Tour> tours = eventService.getTours();
        List<Tour> tourApplications = eventService.getTourApplications();

        model.addAttribute("tours", tours);
        model.addAttribute("tourApplications", tourApplications);
        model.addAttribute("responsibleDay", responsibleDay);
        System.out.println("ASHGSYdgsydfewyhd");
        System.out.println(responsibleDay);
        System.out.println("ASHGSYdgsydfewyhd");
        //model.addAttribute("advisorId", advisorId);
        return "advisor-tables";
    }


    @GetMapping("/head-secretary-tables")
    public String showEventListHeadSecretary(Model model) {

        model.addAttribute("tourApplications", eventService.getTourApplications());
        return "head-secretary-tables";
    }

}
