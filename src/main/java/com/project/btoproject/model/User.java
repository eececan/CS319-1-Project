package com.project.btoproject.model;

import com.project.btoproject.model.UserTask;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "all_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

    @Id
    private Long id; //is this school id or automatically generated id

    @Column(name = "password", nullable = true)
    private String password = "";

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", unique = true, nullable = true)
    private String email;

    @Column(name = "picture")
    private String picture;

    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTask> tasks;
}

