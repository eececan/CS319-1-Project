// src/main/java/com/project/btoproject/model/Tour.java

package com.project.btoproject.model;

import com.project.btoproject.enums.Hour;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("TOUR")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class Tour extends Event {

    @CollectionTable(name = "tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "hour")
    private String availableHour;

    @Column(name = "people_count")
    private int peopleCount;

    @ManyToMany
    @JoinTable(
            name = "tour_guides",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "guide_id")
    )
    private List<Guide> guides;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    /*@OneToOne
    @JoinColumn(name = "school_id")
    private School school;*/

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_info_id")
    private TourInfo tourInformation;
}
