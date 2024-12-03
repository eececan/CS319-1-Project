package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.Event;
import com.project.btoproject.model.Fair;
import com.project.btoproject.model.Tour;
import com.project.btoproject.model.TourInfo;
import com.project.btoproject.repository.IEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private IEventRepository eventRepository;

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
}
