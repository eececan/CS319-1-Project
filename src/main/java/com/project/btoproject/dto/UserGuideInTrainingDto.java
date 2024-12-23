package com.project.btoproject.dto;

import com.project.btoproject.model.UserTask;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserGuideInTrainingDto {
    private String schedule = "default_schedule"; // assuming Hour[] is mapped as a string
    private String department;
    private Integer grade;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String picture;
    private String description;
    private Date startDate;
}
