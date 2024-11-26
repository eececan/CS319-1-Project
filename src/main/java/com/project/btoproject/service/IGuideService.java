package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;

public interface IGuideService {
    Event[] seeAssignedEvents();
    void setStatusOfTour(Tour t);
    void changeTourInformation(Tour t);
    Advisor seeAdvisorOfDay();
    void selfAssignTour(Tour t);
    void selfAssignIndividualTour(IndividualTour t);
}
