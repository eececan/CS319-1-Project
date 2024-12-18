package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "all_users")
@Getter
@Setter
@DiscriminatorValue("GUIDE")
public class Guide extends User {

    @Column(name = "schedule")
    private String schedule = "default_schedule"; // assuming Hour[] is mapped as a string

    @Column(name = "department", nullable = true)
    private String department = "default_department";

    @Column(name = "grade")
    private Integer grade = 2;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PointRecord> points = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "event_guides",
            joinColumns = @JoinColumn(name = "guide_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();
}
