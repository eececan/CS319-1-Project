package com.project.btoproject.model;

import com.project.btoproject.enums.SchoolType;
import com.project.btoproject.enums.Tier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tier")
    private Tier tier = Tier.THIRD_TIER;

    //link this to counselor tables
    @Column(name = "counselor_id")
    private Long counselorId;

    @Column(name = "flag")
    private boolean flag;

    @Column(name = "school_type", nullable = true)
    private SchoolType schoolType;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();
    // We might also need to add fairs
}