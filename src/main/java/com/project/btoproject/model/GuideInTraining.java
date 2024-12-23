package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "all_users")
@DiscriminatorValue("GUIDE_IN_TRAINING")
public class GuideInTraining extends User {

    /** Schedule of availability, mapped as a string. */
    @Column(name = "schedule",nullable = true)
    private String schedule = "eeeeeeeeeeeeeeeeeeeeeeeeeeee";

    /** Department of the guide in training . */
    @Column(name = "department", nullable = true)
    private String department;

    /** Grade of the guide in training */
    @Column(name = "grade", nullable = true)
    private Integer grade;

    /** Indicates whether training is complete. Default is false. */
    @Column(name = "training_complete", nullable = true)
    private boolean trainingComplete = false;

    /** Events assigned to the guide in training. */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private List<Event> events;
}
