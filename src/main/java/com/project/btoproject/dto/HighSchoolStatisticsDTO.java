package com.project.btoproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighSchoolStatisticsDTO {
    private String highSchoolName;
    private Long studentCount;
}
