package com.project.btoproject.service;

import com.project.btoproject.model.*;
import com.project.btoproject.repository.IGuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
            //touru guncelle
        }
        //maybe add error message later
    }

    /*@Override
    @Transactional
    public void selfAssignIndividualTour(Guide guide, IndividualTour individualTour) {
        if(individualTour.getGuide()!=null) {
            throw new IllegalStateException("This individual tour already has a guide assigned.");
        }
        individualTour.setGuide(guide);
        guide.getEvents().add(individualTour);
        guideRepository.save(guide);
    }*/

    @Override
    public int seeCurrentPoints(Guide guide) {
        int totalPoints = 0;
        for(PointRecord pointRecord : guide.getPoints()) {
            totalPoints += pointRecord.getPoint();
        }
        return totalPoints;
    }

    @Override
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    @Override
    public void deleteGuide(Long guideId) {
        guideRepository.deleteById(guideId);
    }

    @Override
    public List<Guide> getGuidesByDepartment(String department) {
        return guideRepository.findAllByDepartment(department);
    }

    @Override
    public List<Long> getGuideRankings() {
        return guideRepository.findAll()
                .stream()
                .sorted((g1, g2) -> Integer.compare(getTotalPoints(g2), getTotalPoints(g1)))
                .map(Guide::getId)
                .toList();
    }

    @Override
    public List<Guide> getGuideRankingsEntity() {
        return guideRepository.findAll()
                .stream()
                .sorted((g1, g2) -> Integer.compare(getTotalPoints(g2), getTotalPoints(g1)))
                .toList();
    }
    @Override
    public Guide getGuideWithLowestPoints() {
        List<Long> rankings = getGuideRankings();
        Long guideId = rankings.get(rankings.size()-1);
        return guideRepository.findById(guideId).orElse(null);
    }

    @Override
    public Guide getGuideWithHighestPoints() {
        List<Long> rankings = getGuideRankings();
        Long guideId = rankings.get(0);
        return guideRepository.findById(guideId).orElse(null);
    }

    @Override
    public int getGuideRanking(Long guideId) {
        List<Long> rankings = getGuideRankings();
        //1st for highest points
        return rankings.indexOf(guideId) +1;
    }

    @Override
    public int getTotalPoints(Guide guide) {
        return guide.getPoints().stream().mapToInt(PointRecord::getPoint).sum();
    }

    @Override
    public Guide getGuideByName(String firstName, String lastName) {
        return guideRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found with name: " + firstName + " " + lastName));
    }
    @Override
    public Guide getGuideById(Long id) {
        return guideRepository.getGuideById(id);
    }
}
