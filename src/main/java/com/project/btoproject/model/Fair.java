package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;
import com.project.btoproject.model.School;
import lombok.experimental.SuperBuilder;
import java.util.Date;

@Data
@Entity
@DiscriminatorValue("FAIR")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class Fair extends Event {

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    private School school;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User[] responsibleMembers;

    @Column(name = "fair_info")
    private String fairInfo;

    @Column(name = "hour")
    private String hour;

    @Column(name = "date")
    private Date date;
}