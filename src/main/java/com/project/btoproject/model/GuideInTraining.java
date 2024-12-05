package com.project.btoproject.model;

import com.project.btoproject.common.PointRecord;
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

    @Column(name = "schedule",nullable = true)
    private String schedule = "default_schedule"; // assuming Hour[] is mapped as a string

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "training_complete", nullable = false)
    private boolean trainingComplete;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private List<Event> events;
}
