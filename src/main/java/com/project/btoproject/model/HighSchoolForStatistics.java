package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "high_school")
public class HighSchoolForStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "student_count", nullable = false)
    private Long studentCount;
    @Transient  // This field is not persisted in the database
    private Double preferenceRate;  // Calculated only for view purposes
}
