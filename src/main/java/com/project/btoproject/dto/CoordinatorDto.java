package com.project.btoproject.dto;

import com.project.btoproject.model.UserTask;

import java.util.Date;
import java.util.List;

public class CoordinatorDto {
    public String schedule = "default_schedule";
    public String department = "default_department";
    public Integer grade = 3;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String picture;
    public Date startDate;
    public String description;
    public List<UserTask> tasks;
}
