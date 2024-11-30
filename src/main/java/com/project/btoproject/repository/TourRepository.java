package com.project.btoproject.repository;

import com.project.btoproject.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Query to check if a tour with a specific timestamp exists
    boolean existsByApplicationTimeStamp(Date applicationTimeStamp);


    @Query("SELECT MAX(e.applicationTimeStamp) FROM Event e WHERE e.class = Tour")
    Date findLatestApplicationTimeStamp();

}
