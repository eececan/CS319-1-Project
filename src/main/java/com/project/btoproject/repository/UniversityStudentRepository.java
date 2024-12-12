package com.project.btoproject.repository;

import com.project.btoproject.model.HighSchoolForStatistics;
import com.project.btoproject.model.UniversityStudentForStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityStudentRepository extends JpaRepository<UniversityStudentForStatistics, Long> {

    // Custom query to count students for a particular high school
    List<Object[]> countStudentsByHighSchool(HighSchoolForStatistics highSchoolForStatistics);
}
