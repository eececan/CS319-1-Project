package com.project.btoproject.service;

import com.project.btoproject.common.PointRecord;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IGuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuideService implements IGuideService
{
    private final IGuideRepository guideRepository;

    public void saveGuide(Guide guide) {
        guideRepository.save(guide);
    }

    public List<Event> seeAssignedEvents(Guide guide) {
        List<Event> events = guide.getEvents();
        return events;
    }

    @Transactional
    public void selfAssignTour(Guide guide, Tour tour) {
        if (!tour.getGuides().contains(guide)) {
            tour.getGuides().add(guide);
            guide.getEvents().add(tour);
            guideRepository.save(guide);
        }
    }

    @Transactional
    public void selfAssignIndividualTour(Guide guide, IndividualTour individualTour) {
        if(individualTour.getGuide()!=null) {
            throw new IllegalStateException("This individual tour already has a guide assigned.");
        }
        individualTour.setGuide(guide);
        guide.getEvents().add(individualTour);
        guideRepository.save(guide);
    }

    public int seeCurrentPoints(Guide guide) {
        int totalPoints = 0;
        for(PointRecord pointRecord : guide.getPoints()) {
            totalPoints += pointRecord.getPoint();
        }
        return totalPoints;
    }

    public Advisor seeAdvisorOfDay() {
        //TODO
        return null;
    }
}
