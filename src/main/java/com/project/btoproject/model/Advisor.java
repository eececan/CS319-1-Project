package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "new_advisors")
public class Advisor {
    @Id
    @Column(name = "advisor_id")
    private int id; //sadece datetypeda gorunulmesi icin simdilik boyle yaptim
}
