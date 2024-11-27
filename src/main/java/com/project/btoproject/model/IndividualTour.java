// src/main/java/com/project/btoproject/model/IndividualTour.java

package com.project.btoproject.model;
import com.project.btoproject.enums.Hour;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("INDIVIDUAL_TOUR")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class IndividualTour extends Event {



    @CollectionTable(name = "individual_tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "hour")
    private String availableHour;

    @Column(name = "people_count")
    private int peopleCount;

    @Column(name = "student_department")
    private String studentDepartment;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    /*@OneToMany
    @JoinColumn(name = "individual_tour_id")
    private List<Student> participants;*/

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_info_id")
    private TourInfo tourInformation;
}