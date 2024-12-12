package com.project.btoproject.repository;

import com.project.btoproject.model.HighSchoolForStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighSchoolRepository extends JpaRepository<HighSchoolForStatistics, Long> {
    // Find all high schools
    List<HighSchoolForStatistics> findAll();
}
