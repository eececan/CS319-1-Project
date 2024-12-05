package com.project.btoproject.dto;

import com.project.btoproject.enums.EventType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class EventDto {
    private Date date;
    private Status status;
    private String visitorNotes;
    private EventType eventType;
    private List<UserDto> participants;
    private Date applicationTimeStamp;
}
