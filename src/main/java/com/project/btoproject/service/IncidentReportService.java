package com.project.btoproject.service;

import com.project.btoproject.model.IncidentReport;
import com.project.btoproject.repository.IIncidentReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentReportService implements IIncidentReportService {

    private final IIncidentReportRepository incidentReportRepository;

    @Override
    public void saveIncidentReport(IncidentReport incidentReport) {
        incidentReportRepository.save(incidentReport);
    }

    @Override
    public List<IncidentReport> getAllIncidentReports() {
        return incidentReportRepository.findAll();
    }

    @Override
    public void setStatusOfIncidentReport(Long id, String statusOfIncidentReport) {
        IncidentReport report = incidentReportRepository.findById(id).get();
        report.setStatus(statusOfIncidentReport);
        incidentReportRepository.save(report);
    }
}
