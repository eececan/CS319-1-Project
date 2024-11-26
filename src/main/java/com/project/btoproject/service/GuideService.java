package com.project.btoproject.service;

import com.project.btoproject.model.Advisor;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.IndividualTour;
import com.project.btoproject.model.Tour;
import com.project.btoproject.repository.IGuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideService implements IGuideService
{
    private final IGuideRepository guideRepository;

    public Event[] seeAssignedEvents() {
        //TODO
        return null;
    }

    public void setStatusOfTour(Tour t) {
        //TODO
    }

    public void changeTourInformation(Tour t) {
        //TODO
    }

    public Advisor seeAdvisorOfDay() {
        //TODO
        return null;
    }

    public void selfAssignTour(Tour t) {
        //TODO
    }

    public void selfAssignIndividualTour(IndividualTour t) {
        //TODO
    }
}
