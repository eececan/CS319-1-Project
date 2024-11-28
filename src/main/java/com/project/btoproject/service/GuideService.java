package com.project.btoproject.service;

import com.project.btoproject.model.*;
import com.project.btoproject.repository.IGuideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuideService implements IGuideService
{
    private final IGuideRepository guideRepository;

    public Event[] seeAssignedEvents() {
        //TODO 28.11
        return null;
    }

    public Advisor seeAdvisorOfDay() {
        //TODO 28.11
        return null;
    }

    public void selfAssignTour(Tour t) {
        //TODO 28.11
    }

    public void selfAssignIndividualTour(IndividualTour t) {
        //TODO 28.11
    }

    public int seeCurrentPoints(Guide g) {
        //TODO 28.11
        return 0;
    }
}
