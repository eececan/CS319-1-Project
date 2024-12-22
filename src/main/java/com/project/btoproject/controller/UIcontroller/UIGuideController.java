package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import com.project.btoproject.model.GuideInTraining;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.GuideInTrainingService;
import com.project.btoproject.service.GuideService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class UIGuideController {

    private final GuideService guideService;
    private final GuideInTrainingService guideInTrainingService;
    private final AllUsersService allUsersService;

    public UIGuideController(GuideService _guideService, GuideInTrainingService guideInTrainingService, AllUsersService allUsersService) {
        this.guideService = _guideService;
        this.guideInTrainingService = guideInTrainingService;
        this.allUsersService = allUsersService;
    }

    @GetMapping("/getAllGuides")
    public String getAllGuides(Model model) {
        List<Guide> guides = guideService.getAllGuides();
        model.addAttribute("guides", guides);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        return "guides";
    }
    @GetMapping("/getGuideRankings")
    public String getGuideRankings(Model model) {
        List<Guide> guides = guideService.getGuideRankingsEntity();
        model.addAttribute("guides", guides);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        return "guideRankings";
    }
    @GetMapping("/getGuideRankingsDate")
    public String getGuideRankingsDate(Model model) {
        List<Guide> guides = guideService.getGuidesByExperience();
        model.addAttribute("guides", guides);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        return "guideRankingsDate";
    }
    @GetMapping("/getReverseGuideRankings")
    public String getReverseGuideRankings(Model model) {
        List<Guide> guides = guideService.getReverseGuideRankings();
        model.addAttribute("guides", guides);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        return "reverseGuideRankings";
    }
    @GetMapping("/getReverseGuideRankingsDate")
    public String getReverseGuideRankingsDate(Model model) {
        List<Guide> guides = guideService.getGuidesByLowestExperience();
        model.addAttribute("guides", guides);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        return "guideReverseRankingsDate";
    }
    @GetMapping("/updateSchedule")
    public String updateSchedule(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority()) // Get the role name
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        if(role.equals("ROLE_GUIDE")){
            Long guideId = Long.parseLong(username);
            Guide guide = guideService.getGuideById(guideId);
            if(guide!=null){
                if(guide.getSchedule() == null){
                    guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
            }
            else{
                guide = new Guide();
                guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            model.addAttribute("guide", guide);
        }
        else if(role.equals("ROLE_GUIDE_IN_TRAINING")){
            Long guideId = Long.parseLong(username);
            GuideInTraining guide = guideInTrainingService.getGuideInTrainingById(guideId);
            if(guide!=null){
                if(guide.getSchedule() == null){
                    guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
                }
            }
            else{
                guide = new GuideInTraining();
                guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            model.addAttribute("guide", guide);
        }
        else{
            Guide guide = new Guide();
            guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeee");
            model.addAttribute("guide", guide);
        }
        return "guideSchedule";
    }

    @PostMapping("/updateSchedule")
    public String updateSchedule(int position, String status, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority()) // Get the role name
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);
        if(role.equals("ROLE_GUIDE")){
            Long guideId = Long.parseLong(username);
            guideService.setSchedule(guideId, position, status.charAt(0));
            Guide guide = guideService.getGuideById(guideId);
            model.addAttribute("guide", guide);
        }
        else if(role.equals("ROLE_GUIDE_IN_TRAINING")){
            Long guideId = Long.parseLong(username);
            guideInTrainingService.setSchedule(guideId, position, status.charAt(0));
            GuideInTraining guide = guideInTrainingService.getGuideInTrainingById(guideId);
            model.addAttribute("guide", guide);
        }
        else{
            Guide guide = new Guide();
            guide.setSchedule("eeeeeeeeeeeeeeeeeeeeeeeeee");
            model.addAttribute("guide", guide);
        }
        return "guideSchedule";
    }


}
