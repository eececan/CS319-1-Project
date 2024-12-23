package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.IncidentReport;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.service.IIncidentReportService;
import com.project.btoproject.service.IncidentReportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UIIncidentController {

    private final IIncidentReportService incidentReportService;

    public UIIncidentController(IIncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @GetMapping("/getIncidentReports")
    public String getIncidentRecordsPage(Model model) {
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
        List<IncidentReport> reports = incidentReportService.getAllIncidentReports();
        model.addAttribute("all_reports", reports);
        return "incident-report-list";
    }

    @PostMapping("/postIncidentReport")
    public String postIncidentReport(@RequestParam String title, @RequestParam String description, @RequestParam String author, Model model) {
        IncidentReport newIncident = new IncidentReport();
        newIncident.setTitle(title);
        newIncident.setDescription(description);
        newIncident.setAuthor(author);
        incidentReportService.saveIncidentReport(newIncident);
        return "redirect:/getIncidentReports";
    }

    @PostMapping("/setIncidentReportStatus")
    public String setIncidentReportStatus(@RequestParam Long id, @RequestParam String status, Model model) {
        incidentReportService.setStatusOfIncidentReport(id,status);
        return "redirect:/getIncidentReports";
    }
}
