package com.project.btoproject.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private Long id;
    private String picture;
    private Date startDate;
    private String description;
}
