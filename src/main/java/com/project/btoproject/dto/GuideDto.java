package com.project.btoproject.dto;

import com.project.btoproject.model.PointRecord;
import com.project.btoproject.model.UserTask;
import com.project.btoproject.model.Event;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuideDto {
    public String schedule = "default_schedule"; // assuming Hour[] is mapped as a string
    public String department = "default_department";
    public Integer grade = 100;
    public List<PointRecord> points = new ArrayList<>();
    public List<EventDto> events = new ArrayList<>();
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String email;
    public String picture;
    public Date startDate;
    public String description;
    public List<UserTask> tasks;
}
