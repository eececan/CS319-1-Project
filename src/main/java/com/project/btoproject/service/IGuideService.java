package com.project.btoproject.service;

import com.project.btoproject.model.*;

public interface IGuideService {
    Event[] seeAssignedEvents();
    Advisor seeAdvisorOfDay();
    void selfAssignTour(Tour t);
    void selfAssignIndividualTour(IndividualTour t);
    int seeCurrentPoints(Guide g);

}
