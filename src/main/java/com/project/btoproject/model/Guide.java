package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a Guide entity, a type of User responsible for assisting with campus tours.
 * Includes schedule, department, grade, points, and events.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "all_users")
@Getter
@Setter
@DiscriminatorValue("GUIDE")
public class Guide extends User {

    /** Availability schedule ('e' for empty, 'f' for full). */
    @Column(name = "schedule", nullable = true)
    private String schedule = "eeeeeeeeeeeeeeeeeeeeeeeeeeee"; // e is empty f is full

    /** Department of the guide (optional). */
    @Column(name = "department", nullable = true)
    private String department;

    /** Grade of the guide. */
    @Column(name = "grade", nullable = true)
    private Integer grade;

    /** Performance points linked to the guide. */
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PointRecord> points = new ArrayList<>();

    /** Events assigned to the guide. */
    @ManyToMany
    @JoinTable(
            name = "event_guides",
            joinColumns = @JoinColumn(name = "guide_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();
}
