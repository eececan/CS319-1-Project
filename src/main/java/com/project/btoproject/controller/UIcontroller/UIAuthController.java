package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.AuthResponseDTO;
import com.project.btoproject.dto.LoginDto;
import com.project.btoproject.model.Guide;
import com.project.btoproject.service.AuthService;
import com.project.btoproject.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ui/auth")
public class UIAuthController {
    private final AuthService authService;
    private final EventService eventService;

    public UIAuthController(AuthService _authService, EventService eventService) {
        this.authService = _authService;
        this.eventService = eventService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, Model model) {
        System.out.println(loginDto.getPassword());
        System.out.println(loginDto.getUsername());
        ResponseEntity<?> response = authService.login(loginDto);
        if (response.getStatusCode().is2xxSuccessful()) {
            AuthResponseDTO authResponseDTO = (AuthResponseDTO) response.getBody();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("auth", auth);
            // Check user roles and return appropriate page
            if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_DIRECTOR"))) {
                return "Director-Dashboard"; // Director's page
            }
            else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADVISOR"))) {
                return "Advisor-Dashboard"; // Advisor's page
            }
            else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_GUIDE"))) {
                return "Guide-taskboard"; // Guide's page
            }
            else if (auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_HEAD_SECRETARY"))) {
                return "Head-Secretary-Dashboard"; // Guide's page
            } else {
                return "page-empty"; // Default page for unrecognized roles
            }
        }
        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        return "login";
    }

    @GetMapping("/advisor-tables")
    public String showEventListAdvisor(Model model) {

        model.addAttribute("tours", eventService.getAllTours());
        return "advisor-tables";
    }

    @GetMapping("/head-secretary-tables")
    public String showEventListHeadSecretary(Model model) {

        model.addAttribute("tours", eventService.getAllTours());
        return "head-secretary-tables";
    }

}
