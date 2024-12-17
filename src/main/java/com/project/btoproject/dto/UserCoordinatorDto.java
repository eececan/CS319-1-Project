package com.project.btoproject.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//attributes will be added once the coordinator class is added
@Getter
@Setter
public class UserCoordinatorDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String picture;
    private String description;
    //do we store schedule in coordinator but not in advisor?
    private String department;
    private Integer grade;
}
