package com.project.btoproject.repository;

import com.project.btoproject.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByName(String name);
    @Query("SELECT s.name, COUNT(t) FROM School s JOIN s.tours t GROUP BY s.name ORDER BY COUNT(t) DESC")
    List<Object[]> countSchoolsInTours();

    @Query("SELECT s.city, COUNT(t) FROM School s " +
            "JOIN s.tours t GROUP BY s.city ORDER BY COUNT(t) DESC")
    List<Object[]> getTourCountsByState();
}
