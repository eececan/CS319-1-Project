package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Tour;

import java.time.DayOfWeek;
import java.util.List;

public interface IAdvisorService {
    Advisor getAdvisorById(Long advisorId);
    DayOfWeek getResponsibleDay(Long advisorId);
}
