package com.project.btoproject.model;

import com.project.btoproject.common.PointRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "all_users")
@DiscriminatorValue("GUIDE")
public class Guide extends User {

    @Column(name = "schedule")
    private String schedule = "default_schedule"; // assuming Hour[] is mapped as a string

    @Column(name = "department", nullable = false)
    private String department = "default_department";

    @Column(name = "grade")
    private Integer grade = 2;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PointRecord> points = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id")
    private List<Event> events = new ArrayList<>();
}
