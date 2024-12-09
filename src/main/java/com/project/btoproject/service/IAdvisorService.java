package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Guide;

import java.time.DayOfWeek;

public interface IAdvisorService {
    Advisor getAdvisorById(Long advisorId);
    DayOfWeek getResponsibleDay(Long advisorId);
    Advisor saveAdvisor(Advisor advisor);
    Advisor findAdvisorsByResponsibleDay(DayOfWeek day);
    Advisor getAdvisorByName(String firstName, String lastName);
}
