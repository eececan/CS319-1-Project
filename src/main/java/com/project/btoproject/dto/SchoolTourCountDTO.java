package com.project.btoproject.dto;

import com.project.btoproject.enums.Tier;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SchoolTourCountDTO {
    private String schoolName;
    private Long tourCount;
    private Tier tier;
    // Constructor, getters, and setters
    public SchoolTourCountDTO(String schoolName, Long tourCount, Tier tier) {
        this.schoolName = schoolName;
        this.tourCount = tourCount;
        this.tier = tier;
    }

}
