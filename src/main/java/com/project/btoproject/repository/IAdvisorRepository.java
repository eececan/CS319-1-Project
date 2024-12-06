package com.project.btoproject.repository;

import com.project.btoproject.model.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdvisorRepository extends JpaRepository<Advisor, Integer> {
}
