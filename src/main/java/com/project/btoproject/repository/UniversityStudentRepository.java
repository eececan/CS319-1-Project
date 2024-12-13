package com.project.btoproject.repository;

import com.project.btoproject.model.HighSchoolForStatistics;
import com.project.btoproject.model.UniversityStudentForStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityStudentRepository extends JpaRepository<UniversityStudentForStatistics, Long> {

    // Query to get statistics of high schools and number of students who are enrolled at Bilkent University
    @Query("SELECT hs.name, COUNT(u) FROM UniversityStudentForStatistics u " +
            "JOIN u.highSchool hs " +
            "GROUP BY hs.name " +
            "ORDER BY COUNT(u) DESC")
    List<Object[]> countStudentsByHighSchool();
}
