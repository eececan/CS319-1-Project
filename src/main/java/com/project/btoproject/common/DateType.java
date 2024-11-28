package com.project.btoproject.common;

import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Event;
import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DateType {

    private Advisor advisorOfTheDay;

    private boolean isFull;

    private Event[] events;

    private Hour[] timeSlots;

    private LocalDate date;
}
