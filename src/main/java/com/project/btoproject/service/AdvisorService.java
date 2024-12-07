package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.repository.IAdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class AdvisorService implements IAdvisorService {
    private final IAdvisorRepository advisorRepository;

    @Override
    public Advisor getAdvisorById(Long advisorId) {
        return advisorRepository.getAdvisorById(advisorId);
    }

    @Override
    public DayOfWeek getResponsibleDay(Long advisorId) {
        Advisor advisor = getAdvisorById(advisorId);
        return advisor.getResponsibleDay();
    }

}
