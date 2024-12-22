package com.project.btoproject.controller.UIcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIHomeController {
    @GetMapping("/home")
    public String home() {
        return "information-office-page";
    }
    @GetMapping("/")
    public String login() {
        return "login";
    }
}
