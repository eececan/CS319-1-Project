package com.project.btoproject.common;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "point_record")
public class PointRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "point", nullable = false)
    private int point;
}