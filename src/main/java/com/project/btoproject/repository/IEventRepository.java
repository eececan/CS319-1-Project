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
                            /*--- TOUR APPLICATION SPECIFIC QUERIES START ------*/

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND TO_CHAR(t.date, 'D') = :day ORDER BY t.applicationTimeStamp DESC, t.status ASC, t.hour ASC")
    Page<Tour> findTourApplicationsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses ORDER BY t.applicationTimeStamp DESC, t.status ASC, t.hour ASC")
    Page<Tour> findTourApplicationsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY t.applicationTimeStamp DESC, t.status ASC, t.hour ASC")
    Page<Tour> findTourApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
                            /*--- TOUR APPLICATION SPECIFIC QUERIES END ------*/


                            /*--- TOUR SPECIFIC QUERIES START ------*/

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses")
    List<Tour> findToursByStatuses(@Param("statuses") List<Status> statuses);

    @Query("SELECT t FROM Tour t WHERE t.school.name LIKE %:name%")
    List<Tour> findAllBySchoolNameContaining(@Param("name") String name);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses  ORDER BY t.status ASC ,t.date ASC,t.hour ASC")
    Page<Tour> findToursByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND TO_CHAR(t.date, 'D') = :day ORDER BY t.status ASC ,t.date ASC,t.hour ASC")
    Page<Tour> findToursByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY t.status ASC ,t.date ASC, t.hour ASC")
    Page<Tour> findToursByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    @Query("SELECT t FROM Tour t WHERE t.status IN :statuses AND LOWER(t.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(t.date, 'D') = :day ORDER BY t.status ASC ,t.date ASC, t.hour ASC")
    Page<Tour> findToursByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
                        /*--- TOUR SPECIFIC QUERIES END ------*/

                         /*--- FAIR SPECIFIC QUERIES START ------*/

    @Query("SELECT t FROM Fair t WHERE t.status IN :statuses")
    List<Fair> findFairsByStatuses(@Param("statuses") List<Status> statuses);

    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND TO_CHAR(f.date, 'D') = :day ORDER BY f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND TO_CHAR(f.date, 'D') = :day ORDER BY f.applicationTimeStamp DESC, f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairApplicationsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses ORDER BY f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses ORDER BY f.applicationTimeStamp DESC,f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairApplicationsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(f.date, 'D') = :day ORDER BY f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairsByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(f.date, 'D') = :day ORDER BY f.applicationTimeStamp DESC, f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairApplicationsByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    @Query("SELECT f FROM Fair f WHERE f.status IN :statuses AND LOWER(f.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY f.status ASC ,f.date ASC, f.hour ASC")
    Page<Fair> findFairApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
                    /*--- FAIR SPECIFIC QUERIES END ------*/

                     /*--- INDIVIDUAL TOUR SPECIFIC QUERIES START ------*/
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND TO_CHAR(it.date, 'D') = :day ORDER BY it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualToursByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);

    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND TO_CHAR(it.date, 'D') = :day ORDER BY it.applicationTimeStamp DESC,it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualTourApplicationsByStatusesAndDayPageable(@Param("statuses") List<Status> statuses, @Param("day") String day, Pageable pageable);
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses ORDER BY it.status ASC ,it.date ASC, it.hour ASC")
    List<IndividualTour> findIndividualToursByStatuses(@Param("statuses") List<Status> statuses);


    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses ORDER BY it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualToursByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses ORDER BY it.applicationTimeStamp DESC, it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualTourApplicationsByStatusesPageable(@Param("statuses") List<Status> statuses, Pageable pageable);
    // Individual Tour Applications Search
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY it.applicationTimeStamp DESC,it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualTourApplicationsByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // Individual Tours Search
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))ORDER BY it.status ASC ,it.date ASC, it.hour ASC")
    Page<IndividualTour> findIndividualToursByStatusesAndSearchTerm(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );


    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(it.date, 'D') = :day ORDER BY it.status ASC ,it.date ASC, it.hour ASC ")
    Page<IndividualTour> findIndividualToursByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
    @Query("SELECT it FROM IndividualTour it WHERE it.status IN :statuses AND LOWER(it.student.school.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND TO_CHAR(it.date, 'D') = :day ORDER BY it.applicationTimeStamp DESC, it.status ASC ,it.date ASC, it.hour ASC ")
    Page<IndividualTour> findIndividualTourApplicationsByStatusesAndSearchTermAndDay(
            @Param("statuses") List<Status> statuses,
            @Param("searchTerm") String searchTerm,
            @Param("day") String day,
            Pageable pageable
    );
                 /*--- INDIVIDUAL TOUR SPECIFIC QUERIES END ------*/
}
