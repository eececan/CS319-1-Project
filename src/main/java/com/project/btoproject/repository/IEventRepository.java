package com.project.btoproject.repository;

import com.project.btoproject.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

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

}
