package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private IEventRepository eventRepository;

    @Autowired
    public EventService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    public void setStatusOfTour(Tour t, Status status) {
        Optional<Tour> tourOptional = eventRepository.findById(t.getId())
                .filter(event -> event instanceof Tour)
                .map(event -> (Tour) event);

        if (tourOptional.isPresent()) {
            t.setStatus(status);
        }
        else {
            throw new IllegalArgumentException("Tour not found.");
        }
    }
    public void setStatusOfEvent(Event e, Status status) {
        Optional <Event> eventOptional = eventRepository.findById(e.getId());
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event instanceof Tour) {
                Tour tour = (Tour) event;
                tour.setStatus(status);
                //Save using TourRepository if specific actions are needed
                //tourRepository.save(tour); // TOUR_REPOSITORY DELETED
            } else if (event instanceof IndividualTour) {
                IndividualTour individualTour = (IndividualTour) event;
                individualTour.setStatus(status);
            } else if (event instanceof Fair) {
                Fair fair = (Fair) event;
                fair.setStatus(status);

            }
            eventRepository.save(event);
        } else {

            throw new IllegalArgumentException("Event not found.");
        }
    }
    public Status getStatusOfEvent(Event e) {
        Optional <Event> eventOptional = eventRepository.findById(e.getId());

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.getStatus();
        } else {
            throw new IllegalArgumentException("Event not found.");
        }
    }

    public void changeTourInformation(Tour t, TourInfo tourInfo) {
        Optional<Tour> tourOptional = eventRepository.findById(t.getId())
                .filter(event -> event instanceof Tour)
                .map(event -> (Tour) event);

        if (tourOptional.isPresent()) {
            t.setTourInformation(tourInfo);
        }
        else {
            throw new IllegalArgumentException("Tour not found.");
        }
    }

    @Override
    public void changeFairInformation(Fair f, String fairInfo) {
        Optional<Fair> fairOptional = eventRepository.findById(f.getId())
                .filter(event -> event instanceof Fair)
                .map(event -> (Fair) event);

        if (fairOptional.isPresent()) {
            f.setFairInfo(fairInfo);
        }
        else {
            throw new IllegalArgumentException("Fair not found.");
        }
    }

    @Override
    public List<Fair> seeUpcomingFairs() {
        List<Event> allEvents = eventRepository.findAll();
        Date today = new Date();
        return allEvents.stream()
                .filter(event -> event instanceof Fair)
                .filter(event -> event.getDate() != null && event.getDate().after(today))
                .map(event -> (Fair) event)
                .toList();
    }

    @Override
    public void sendFairReminderToResponsibleMembers() {
        //TODO
    }

    @Override
    public String seeRemainingTimeUntilEvent(Event e) {
        if (e.getDate()==null) {
            return ("Event date is not set");
        }

        Date now = new Date();
        if (e.getDate().before(now)) {
            return ("The event has already passed");
        }

        long differenceInMs = e.getDate().getTime()-now.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(differenceInMs);
        long hours = TimeUnit.MILLISECONDS.toHours(differenceInMs)%24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMs)%60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInMs)%60;

        String remainingTime = String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
        return ("Remaining time until the event: " + remainingTime);
    }

    @Override
    public Date findLatestFairApplicationTimeStamp() {
        return eventRepository.findLatestFairApplicationTimeStamp();
    }

    @Override
    public Date findLatestTourApplicationTimeStamp() {
        return eventRepository.findLatestTourApplicationTimeStamp();
    }

    public void saveAllTours(List<Tour> tours) {
        eventRepository.saveAll(tours); // Save tours as they are also events
    }

    public void saveAllFairs(List<Fair> fairs) {
        eventRepository.saveAll(fairs); // Save fairs as they are also events
    }

    public List<Tour> getAllTours() {
        return eventRepository.findAllTours();
    }

    public List<Fair> getAllFairs() {
        return eventRepository.findAllFairs();
    }

}


