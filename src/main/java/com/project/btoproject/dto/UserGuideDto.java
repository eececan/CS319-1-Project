package com.project.btoproject.dto;

import com.project.btoproject.model.UserTask;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.PointRecord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserGuideDto {
    private String schedule;
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
