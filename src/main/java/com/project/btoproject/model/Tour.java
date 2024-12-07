// src/main/java/com/project/btoproject/model/Tour.java

package com.project.btoproject.model;

import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("TOUR")
// @NoArgsConstructor // I needed a specific empty constructor so I commented this
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class Tour extends Event {

    @CollectionTable(name = "tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "hour", nullable = false)
    @Enumerated(EnumType.STRING) // Store as a string in the database
    private Hour hour;
    //private String availableHour; // Why is this string and what available hour means? Shouldn't we just use Hour enum?

    @Column(name = "people_count")
    private int peopleCount;

    @ManyToMany
    @JoinTable(
            name = "tour_guides",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private List<Guide> guides;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    private School school;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_info_id")
    private TourInfo tourInformation;

    @ManyToOne
    @JoinColumn(name = "school_counselor_id", nullable = false)
    private SchoolCounselor schoolCounselor;

    // Constructor that sets eventType to TOUR
    public Tour() {
        this.setEventType(EventType.TOUR);
        this.setStatus(Status.NEW_TOUR_APPLICATION);
    }

}
