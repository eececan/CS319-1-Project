package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.model.IncidentReport;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.service.IIncidentReportService;
import com.project.btoproject.service.IncidentReportService;
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
}
