package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "tour_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "duration")
    private double duration;

    @Column(name = "guide_comment")
    private String guideComment;

    @Column(name = "feedback")
    private String feedback;

}