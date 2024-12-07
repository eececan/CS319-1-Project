package com.project.btoproject.repository;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;

public interface IAdvisorRepository extends JpaRepository<Advisor, Integer> {
    Advisor getAdvisorById(Long id);
    Advisor findByResponsibleDay(DayOfWeek day);
}
