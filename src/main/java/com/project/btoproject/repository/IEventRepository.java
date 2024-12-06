package com.project.btoproject.repository;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IEventRepository extends JpaRepository<Event, Long> {

    /**
     * Find the latest application timestamp for Fair events.
     */
    @Query("SELECT MAX(t.applicationTimeStamp) FROM Tour t")
    Date findLatestTourApplicationTimeStamp();

    /**
     * Find the latest application timestamp for Fair events.
     */
    @Query("SELECT MAX(f.applicationTimeStamp) FROM Fair f")
    Date findLatestFairApplicationTimeStamp();

    @Query("SELECT e FROM Event e WHERE TYPE(e) = Tour")
    List<Tour> findAllTours();

    @Query("SELECT e FROM Event e WHERE TYPE(e) = Fair")
    List<Tour> findAllFairs();


}
