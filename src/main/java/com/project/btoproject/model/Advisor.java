package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("ADVISOR")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Advisor extends User {

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "grade")
    private Integer grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "responsible_day")
    private DayOfWeek responsibleDay;

    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tour> approvedTours = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "advisor_id")
    private List<Event> events = new ArrayList<>();
}
