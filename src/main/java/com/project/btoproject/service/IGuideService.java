package com.project.btoproject.service;

import com.project.btoproject.model.*;

import java.util.List;

public interface IGuideService {
    void saveGuide(Guide guide);
    List<Event> seeAssignedEvents(Guide g);
    Advisor seeAdvisorOfDay();
    void selfAssignTour(Guide guide, Tour tour);
    void selfAssignIndividualTour(Guide guide, IndividualTour tour);
    int seeCurrentPoints(Guide g);

}
