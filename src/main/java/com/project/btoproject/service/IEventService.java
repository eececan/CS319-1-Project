package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Fair;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.TourInfo;

import java.util.Date;
import java.util.List;

public interface IEventService {
    void setStatusOfTour(Tour t, Status status);
    void changeTourInformation(Tour t, TourInfo tourInfo);
    void changeFairInformation(Fair f, String fairInfo);
    List<Fair> seeUpcomingFairs();

    List<Tour> seeUpcomingTours();

    void sendFairReminderToResponsibleMembers();

    void sendTourReminderToGuides();

    String seeRemainingTimeUntilEvent(Event e);
    public Date findLatestFairApplicationTimeStamp();
    public Date findLatestTourApplicationTimeStamp();
}
