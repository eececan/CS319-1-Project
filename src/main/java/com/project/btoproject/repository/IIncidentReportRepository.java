package com.project.btoproject.repository;

import com.project.btoproject.model.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIncidentReportRepository extends JpaRepository<IncidentReport, Long> {

}
