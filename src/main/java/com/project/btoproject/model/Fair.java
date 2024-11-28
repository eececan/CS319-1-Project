package com.project.btoproject.model;

import jakarta.persistence.*;
import lombok.*;
import com.project.btoproject.model.School;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@DiscriminatorValue("EVENT")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@EqualsAndHashCode(callSuper = true)
public class Fair extends Event {

//    @Column(name = "school")
//    private School school;

    @Column(name = "address")
    private String address;

//    @Column(name = "responsible_members")
//    private User[] responsibleMembers;

    @Column(name = "fair_info")
    private String fairInfo;

    @Column(name = "hour")
    private String hour;
}