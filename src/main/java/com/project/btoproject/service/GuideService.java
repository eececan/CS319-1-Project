package com.project.btoproject.service;

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

    @Override
    public void saveGuide(Guide guide) {
        guideRepository.save(guide);
    }

    @Override
    public List<Event> seeAssignedEvents(Guide guide) {
        List<Event> events = guide.getEvents();
        return events;
    }

    @Override
    @Transactional
    public void selfAssignTour(Guide guide, Tour tour) {
        if (!tour.getGuides().contains(guide)) {
            tour.getGuides().add(guide);
            guide.getEvents().add(tour);
            guideRepository.save(guide);
        }
        //maybe add error message later
    }

    @Override
    @Transactional
    public void selfAssignIndividualTour(Guide guide, IndividualTour individualTour) {
        if(individualTour.getGuide()!=null) {
            throw new IllegalStateException("This individual tour already has a guide assigned.");
        }
        individualTour.setGuide(guide);
        guide.getEvents().add(individualTour);
        guideRepository.save(guide);
    }

    @Override
    public int seeCurrentPoints(Guide guide) {
        int totalPoints = 0;
        for(PointRecord pointRecord : guide.getPoints()) {
            totalPoints += pointRecord.getPoint();
        }
        return totalPoints;
    }

    @Override
    public Advisor seeAdvisorOfDay() {
        //TODO
        return null;
    }

    @Override
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    @Override
    public Guide getGuideByName(String firstName, String lastName) {
        return guideRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found with name: " + firstName + " " + lastName));
    }
    @Override
    public Guide getGuideById(long id) {
        return guideRepository.getGuideById(id);
    }
}
