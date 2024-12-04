package com.project.btoproject.model;

import com.project.btoproject.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import com.project.btoproject.model.School;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Data
@Entity
@DiscriminatorValue("FAIR")
//@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class Fair extends Event {

    @CollectionTable(name = "fair", joinColumns = @JoinColumn(name = "event_id"))

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    private School school;

    @ManyToOne
    @JoinColumn(name = "school_counselor_id", nullable = false)
    private SchoolCounselor schoolCounselor;

    @Column(name = "hour")
    private String hour;

    @Column(name = "people_count")
    private int peopleCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User[] responsibleMembers;

    @Column(name = "fair_info")
    private String fairInfo;

    // Constructor that sets eventType to FAIR
    public Fair() {
        this.setEventType(EventType.FAIR);
    }
}