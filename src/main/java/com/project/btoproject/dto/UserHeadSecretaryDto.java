package com.project.btoproject.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class UserHeadSecretaryDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String picture;
    private Date startDate;
    private String description;
}
