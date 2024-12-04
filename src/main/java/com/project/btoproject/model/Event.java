package com.project.btoproject.model;

import com.project.btoproject.common.DateType;
import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Builder
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "date")
    //private DateType date;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE) // Store as a DATE type in the database
    private Date date;

    @Column(name = "status")
    private Status status;

    // Changing event_description to visitor_notes
    @Column(name= "visitor_notes")
    private String visitorNotes;

    @Column(name = "event_type")
    private EventType eventType;

    @ManyToMany
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    // Timestamp field that is set manually
    @Column(name = "applicationTimeStamp", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationTimeStamp;
}
