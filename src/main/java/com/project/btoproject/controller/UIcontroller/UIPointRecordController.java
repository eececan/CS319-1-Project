package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.model.User;
import com.project.btoproject.service.IAllUsersService;
import com.project.btoproject.service.IGuideService;
import com.project.btoproject.service.IPointRecordService;
import com.project.btoproject.service.IUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UIPointRecordController {
    private final IGuideService guideService;
    private final IPointRecordService pointRecordService;

    public UIPointRecordController(IGuideService guideService, IPointRecordService pointRecordService) {
        this.guideService = guideService;
        this.pointRecordService = pointRecordService;
    }

    @GetMapping("/getAllRecords")
    public String getAllPointRecordsPage(Model model) {
        List<PointRecord> records = pointRecordService.findAllRecords().stream().toList();
        model.addAttribute("all_records", records);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
            model.addAttribute("role", role);
        }
        return "all-point-record-list";
    }

    // Get point records page
    @GetMapping("/getRecordsOfGuide")
    public String getPointRecordPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role = "";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
            model.addAttribute("role", role);
        }

        Guide guide = guideService.getGuideById(Long.parseLong(username));
        List<PointRecord> pointRecords = pointRecordService.getPointRecordsByGuide(guide);
        model.addAttribute("guide_records", pointRecords);
        model.addAttribute("guide", guide);
        return "point-record-list";
    }

}
