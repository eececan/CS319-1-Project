package com.project.btoproject.service;

import com.project.btoproject.model.IncidentReport;

import java.util.List;

public interface IIncidentReportService {
    void saveIncidentReport(IncidentReport incidentReport);
    List<IncidentReport> getAllIncidentReports();
    void setStatusOfIncidentReport(Long id, String statusOfIncidentReport);
}
