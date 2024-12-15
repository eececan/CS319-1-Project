package com.project.btoproject.model;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("INDIVIDUAL_TOUR")
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class IndividualTour extends Event {

    @CollectionTable(name = "individual_tour", joinColumns = @JoinColumn(name = "event_id"))

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;

    @Column(name = "hour", nullable = false)
    private String hour;

    @Column(name = "interested_field")
    private String interestedField;

    // Constructor that sets eventType to TOUR
    public IndividualTour() {
        this.setEventType(EventType.INDIVIDUAL_TOUR);
        this.setStatus(Status.NEW_INDIVIDUAL_TOUR_APPLICATION);
        this.setGuideCount(1);
    }

    /*@CollectionTable(name = "tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "hour", nullable = false)
    @Enumerated(EnumType.STRING) // Store as a string in the database
    private Hour hour;*/

    /*@CollectionTable(name = "individual_tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "available_hours")
    private String availableHour; *///discuss

    /*@Column(name = "people_count")
    private int peopleCount;*/

    /*@ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;*/

    /*@OneToMany
    @JoinColumn(name = "individual_tour_id")
    private List<Student> studentParticipants;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_info_id")
    private TourInfo tourInformation;*/


}