package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Guide;
import com.project.btoproject.repository.IAdvisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvisorService implements IAdvisorService {
    private final IAdvisorRepository advisorRepository;

    public List<Advisor> getAllAdvisors() {
        return advisorRepository.findAll();
    }

    @Override
    public Advisor getAdvisorById(Long advisorId) {
        return advisorRepository.getAdvisorById(advisorId);
    }

    @Override
    public DayOfWeek getResponsibleDay(Long advisorId) {
        Advisor advisor = getAdvisorById(advisorId);
        return advisor.getResponsibleDay();
    }

    public Advisor saveAdvisor(Advisor advisor) {
        return advisorRepository.save(advisor);
    }
    @Override
    public Advisor findAdvisorsByResponsibleDay(DayOfWeek day) {
        return advisorRepository.findByResponsibleDay(day);
    }
    public Advisor getAdvisorByName(String firstName, String lastName) {
        return advisorRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new IllegalArgumentException("Advisor not found with name: " + firstName + " " + lastName));
    }
}
