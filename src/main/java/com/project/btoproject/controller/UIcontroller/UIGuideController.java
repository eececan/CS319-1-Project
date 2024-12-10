package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Guide;
import com.project.btoproject.service.GuideService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UIGuideController {

    private final GuideService guideService;

    public UIGuideController(GuideService _guideService) {
        this.guideService = _guideService;
    }

    @GetMapping("/getAllGuides")
    public String getAllGuides(Model model) {
        List<Guide> guides = guideService.getAllGuides();
        model.addAttribute("guides", guides);

        return "guides";
    }
}
