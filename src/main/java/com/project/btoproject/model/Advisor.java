package com.project.btoproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Advisor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //sadece datetypeda gorunulmesi icin simdilik boyle yaptim
}
