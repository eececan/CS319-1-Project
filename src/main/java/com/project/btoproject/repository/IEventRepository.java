package com.project.btoproject.repository;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Fair;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.Advisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * Find the latest application timestamp for IndividualTour events.
     */
    @Query("SELECT MAX(t.applicationTimeStamp) FROM IndividualTour t")
    Date findLatestIndividualTourApplicationTimeStamp();



    @Query("SELECT e FROM Event e WHERE TYPE(e) = Tour")
    List<Tour> findAllTours();

    @Query("SELECT e FROM Event e WHERE TYPE(e) = Fair")
    List<Fair> findAllFairs();

    @Query("SELECT e FROM Event e WHERE TYPE(e) = IndividualTour ")
    List<IndividualTour> findAllIndividualTours();
    @Query("SELECT e FROM Event e WHERE FUNCTION('DAYOFWEEK', e.date) = :day")
    List<Event> findAllByDayOfWeek(@Param("day") int day);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses")
    List<Tour> findToursByStatuses(@Param("statuses") List<Status> statuses);

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses")
    List<IndividualTour> findIndividualToursByStatuses(@Param("statuses") List<Status> statuses);
    @Query("SELECT t FROM Fair t WHERE t.status IN :statuses")
    List<Fair> findFairsByStatuses(@Param("statuses") List<Status> statuses);


}
