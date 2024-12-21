package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import com.project.btoproject.service.GuideService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
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
    @GetMapping("/getGuideRankings")
    public String getGuideRankings(Model model) {
        List<Guide> guides = guideService.getGuideRankingsEntity();
        model.addAttribute("guides", guides);
        return "guideRankings";
    }
    @GetMapping("/getGuideRankingsDate")
    public String getGuideRankingsDate(Model model) {
        List<Guide> guides = guideService.getGuidesByExperience();
        model.addAttribute("guides", guides);
        return "guideRankingsDate";
    }
    @GetMapping("/getReverseGuideRankings")
    public String getReverseGuideRankings(Model model) {
        List<Guide> guides = guideService.getReverseGuideRankings();
        model.addAttribute("guides", guides);
        return "reverseGuideRankings";
    }
    @GetMapping("/getReverseGuideRankingsDate")
    public String getReverseGuideRankingsDate(Model model) {
        List<Guide> guides = guideService.getGuidesByLowestExperience();
        model.addAttribute("guides", guides);
        return "guideReverseRankingsDate";
    }
    @GetMapping("/updateSchedule")
    public String updateSchedule(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        Long guideId = Long.parseLong(username);
        Guide guide = guideService.getGuideById(guideId);
        model.addAttribute("guide", guide);
        return "guideSchedule";  // Returns the view where the schedule is displayed
    }

    @PostMapping("/updateSchedule")
    public String updateSchedule(int position, String status, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        Long guideId = Long.parseLong(username);
        guideService.setSchedule(guideId, position, status.charAt(0));
        String schedule = guideService.getSchedule(guideId);
        Guide guide = guideService.getGuideById(guideId);
        model.addAttribute("guide", guide);
        return "guideSchedule";  // Returns the updated schedule view
    }


}
