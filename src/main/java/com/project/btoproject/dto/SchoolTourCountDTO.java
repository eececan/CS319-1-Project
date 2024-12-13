package com.project.btoproject.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SchoolTourCountDTO {
    private String schoolName;
    private Long tourCount;

    // Constructor, getters, and setters
    public SchoolTourCountDTO(String schoolName, Long tourCount) {
        this.schoolName = schoolName;
        this.tourCount = tourCount;
    }

}
