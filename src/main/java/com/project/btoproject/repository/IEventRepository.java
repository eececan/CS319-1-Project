package com.project.btoproject.repository;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Fair;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.Advisor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
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
    @Query("SELECT e FROM Event e WHERE TO_CHAR(e.date, 'D') = :day")
    List<Event> findAllByDayOfWeek(@Param("day") String day);
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND TO_CHAR(f.date, 'D') = :day")
    Page<Fair> findFairsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND TO_CHAR(it.date, 'D') = :day")
    Page<IndividualTour> findIndividualToursByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);
    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND TO_CHAR(t.date, 'D') = :day")
    Page<Tour> findToursApplicationsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND TO_CHAR(t.date, 'D') = :day")
    Page<Tour> findToursByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses")
    List<Tour> findToursByStatuses(@Param("statuses") List<Status> statuses);

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses")
    List<IndividualTour> findIndividualToursByStatuses(@Param("statuses") List<Status> statuses);
    @Query("SELECT t FROM Fair t WHERE t.status IN :statuses")
    List<Fair> findFairsByStatuses(@Param("statuses") List<Status> statuses);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses ORDER BY t.status ASC, t.applicationTimeStamp DESC")
    Page<Tour> findToursApplicationsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses  ORDER BY t.status ASC ,t.date ASC")
    Page<Tour> findToursByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.school.name LIKE %:name%")
    List<Tour> findAllBySchoolNameContaining(@Param("name") String name);


    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses ORDER BY f.applicationTimeStamp DESC")
    Page<Fair> findFairsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses ORDER BY it.applicationTimeStamp DESC")
    Page<IndividualTour> findIndividualToursByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);
    // Tour Applications Search
    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Tour> findTourApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Tours Search
    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Tour> findToursByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Fair Applications Search
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Fair> findFairApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Fairs Search
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Fair> findFairsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Individual Tour Applications Search
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<IndividualTour> findIndividualTourApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Individual Tours Search
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<IndividualTour> findIndividualToursByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(t.date, 'D') = :day")
    Page<Tour> findToursByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );

    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(f.date, 'D') = :day")
    Page<Fair> findFairsByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(it.date, 'D') = :day")
    Page<IndividualTour> findIndividualToursByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
}
