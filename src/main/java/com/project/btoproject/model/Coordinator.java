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
@DiscriminatorValue("COORIDNATOR")
public class Coordinator extends User {

    @Column(name = "schedule")
    private String schedule = "default_schedule"; // assuming Hour[] is mapped as a string

    @Column(name = "department", nullable = false)
    private String department = "default_department";

    @Column(name = "grade")
    private Integer grade = 2;

    //decide after discussion
//    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Fair> approvedFairs = new ArrayList<>();
}