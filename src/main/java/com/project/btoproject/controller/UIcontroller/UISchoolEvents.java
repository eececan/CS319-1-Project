package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UISchoolEvents {

    public UISchoolEvents() {

    }
    @GetMapping("/eventjunk")
    public String seeTours(Model model){
        return "events-of-school";
    }
}
