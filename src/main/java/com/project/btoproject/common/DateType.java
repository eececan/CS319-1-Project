package com.project.btoproject.common;

import lombok.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DateType {

    //private Advisor advisorOfTheDay;

    private boolean isFull;

    //private Event[] events;

    //private Hour[] timeSlots;

    private LocalDate date;
}
