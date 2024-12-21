package com.project.btoproject.controller.UIcontroller;

import org.springframework.web.bind.annotation.GetMapping;

public class UIInformationPageController {

    @GetMapping("/kampusTurları")
    public String kampusTurlar() {
        return "campus_tour_page";
    }
    @GetMapping("/fuarDavet")
    public String fuarlar() {
        return "fair_invitation_page";
    }

}
