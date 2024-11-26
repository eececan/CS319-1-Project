package com.project.btoproject.model;

import com.project.btoproject.common.PointRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "guides")
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Inheritance(strategy = InheritanceType.JOINED)
public class Guide extends User {

    @Column(name = "schedule")
    private String schedule; // assuming Hour[] is mapped as a string

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "grade")
    private int grade;

//    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<PointRecord> points;
//
//    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Event> events;
}
