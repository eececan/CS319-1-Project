package com.project.btoproject.dto;

import com.project.btoproject.model.UserTask;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
public class UserAdvisorDto {
    private String department;
    private Integer grade;
    private DayOfWeek responsibleDay;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String picture;
    private String description;
}
