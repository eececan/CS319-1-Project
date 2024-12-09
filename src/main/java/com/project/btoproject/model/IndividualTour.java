package com.project.btoproject.model;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("INDIVIDUAL_TOUR")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class IndividualTour extends Event {

    @CollectionTable(name = "tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "hour", nullable = false)
    @Enumerated(EnumType.STRING) // Store as a string in the database
    private Hour hour;

    @CollectionTable(name = "individual_tour", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "available_hours")
    private String availableHour; //discuss

    @Column(name = "people_count")
    private int peopleCount;

    @Column(name = "student_department")
    private String studentDepartment;

    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    @OneToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @OneToMany
    @JoinColumn(name = "individual_tour_id")
    private List<Student> studentParticipants;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_info_id")
    private TourInfo tourInformation;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_person")
    private String contactPerson;
}