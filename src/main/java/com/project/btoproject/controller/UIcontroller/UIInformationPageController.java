package com.project.btoproject.controller.UIcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIInformationPageController {

    @GetMapping("/kampusTurlari")
    public String kampusTurlar() {
        return "campus_tour_page";
    }
    @GetMapping("/fuarDavet")
    public String fuarlar() {
        return "fair_invitation_page";
    }

}
