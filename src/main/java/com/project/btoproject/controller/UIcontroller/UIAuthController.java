package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.AuthResponseDTO;
import com.project.btoproject.dto.LoginDto;
import com.project.btoproject.model.Guide;
import com.project.btoproject.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("ui/auth")
public class UIAuthController {
    private final AuthService authService;

    public UIAuthController(AuthService _authService) {
        this.authService = _authService;
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
            return "Director-Dashboard";
        }
        model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        return "login";
    }



}
