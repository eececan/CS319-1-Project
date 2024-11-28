package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.TourInfo;

public interface IEventService {
    void setStatusOfTour(Tour t, Status status);
    void changeTourInformation(Tour t, TourInfo tourInfo);

}
