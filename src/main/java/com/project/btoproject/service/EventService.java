package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IEventRepository;
import com.project.btoproject.repository.IGuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private IEventRepository eventRepository;
    private IGuideRepository guideRepository;

    @Autowired
    public EventService(IEventRepository eventRepository, IGuideRepository guideRepository) {
        this.eventRepository = eventRepository;
        this.guideRepository = guideRepository;
    }

    public void approveTourByAdvisor(Long tourId) {
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();
            if (!tour.getStatus().equals(Status.NEW_TOUR_APPLICATION)) {
                throw new IllegalStateException("Tour is not in a state to be approved.");
            }

            tour.setStatus(Status.BTO_ACCEPTED); // Set status to advisor approved
            eventRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + tourId);
        }
    }

    public void rejectTourByAdvisor(Long tourId) {
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();
            if (!tour.getStatus().equals(Status.NEW_TOUR_APPLICATION)) {
                throw new IllegalStateException("Tour is not in a state to be rejected.");
            }

            tour.setStatus(Status.BTO_REJECTED); // Set status to advisor rejected
            eventRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + tourId);
        }
    }

    public void approveTourBySecretary(Long tourId) {
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();
            if (!tour.getStatus().equals(Status.BTO_ACCEPTED)) {
                throw new IllegalStateException("Tour is not in a state to be approved.");
            }

            tour.setStatus(Status.UPCOMING_TOUR); // Set status to advisor approved
            eventRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + tourId);
        }
    }

    public void rejectTourBySecretary(Long tourId) {
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();
            if (!tour.getStatus().equals(Status.BTO_ACCEPTED)) {
                throw new IllegalStateException("Tour is not in a state to be rejected.");
            }

            tour.setStatus(Status.CANCELED_TOUR); // Set status to advisor rejected
            eventRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + tourId);
        }
    }


    public void cancelTourBySecretary(Long tourId) {
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();
            if (!tour.getStatus().equals(Status.UPCOMING_TOUR)) {
                throw new IllegalStateException("Tour is not in a state to be canceled.");
            }

            tour.setStatus(Status.CANCELED_TOUR); // Set status to canceled
            eventRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + tourId);
        }
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
    public List<Tour> seeUpcomingTours() {
        return List.of();
    }

    @Override
    public void sendFairReminderToResponsibleMembers() {
        //TODO
    }

    @Override
    public void sendTourReminderToGuides() {

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
    public List<Event> getEventsByDay(String day) {
        DayOfWeek dayOfWeek;
        try {
            dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }
        int dayNumber = dayOfWeek.getValue(); // 1 (Monday) to 7 (Sunday)
        return eventRepository.findAllByDayOfWeek(dayNumber);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Fair> getAllFairs() {
        return eventRepository.findAllFairs();
    }

    public List<IndividualTour> getAllIndividualTours() {
        return eventRepository.findAllIndividualTours();
    }

    public List<Tour> getTourApplications() {
        List<Status> applicationStatuses = List.of(
                Status.NEW_TOUR_APPLICATION,
                Status.BTO_ACCEPTED,
                Status.BTO_REJECTED,
                Status.UPCOMING_TOUR,
                Status.CANCELED_TOUR
        );
        return eventRepository.findToursByStatuses(applicationStatuses);
    }

    public List<Tour> getTours() {
        List<Status> tourStatuses = List.of(
                Status.UPCOMING_TOUR,
                Status.CANCELED_TOUR,
                Status.COMPLETED_TOUR
        );
        return eventRepository.findToursByStatuses(tourStatuses);
    }

    @Transactional
    public void assignGuideToTour(Long eventId, Long guideId) {


        // Fetch the tour and guide from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found"));
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Check if the guide is already assigned to this tour
        if (event.getGuides().contains(guide)) {
            throw new IllegalArgumentException("This guide is already assigned to this tour.");
        }

        // Check if the guide has another tour on the same date and hour
        boolean hasConflict = guide.getEvents().stream()
                .anyMatch(existingTour -> existingTour.getDate().equals(event.getDate())
                        && ((Tour) existingTour).getHour().equals(((Tour) event).getHour()));
        if (hasConflict) {
            throw new IllegalArgumentException("This guide is already assigned to another tour at the same time.");
        }

        System.out.println("Guide assigned to tour: " + guide.getFirstName() + " " + guide.getLastName());
        // Add the guide to the tour
        (event.getGuides()).add(guide);
        eventRepository.save(event); // Save the updated tour
    }

    @Transactional
    public void increaseGuideCount(Long tourId) {
        // Fetch the tour from the database
        Event event = eventRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found"));

        // Ensure the event is a tour
        if (!(event instanceof Tour)) {
            throw new IllegalArgumentException("The specified event is not a tour.");
        }

        Tour tour = (Tour) event;

        // Check the current guide count
        if (tour .getGuideCount() >= 3) {
            throw new IllegalStateException("The maximum number of guides (3) is already assigned.");
        }

        // Increment the guide count
        tour.setGuideCount(tour.getGuideCount() + 1);

        // Save the updated tour to the database
        eventRepository.save(tour);
    }

    @Transactional
    public void decreaseGuideCount(Long tourId) {
        // Fetch the tour from the database
        Event event = eventRepository.findById(tourId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found"));

        // Decrease guide count but ensure it doesn't go below 1
        if (event.getGuideCount() > 1) {
            event.setGuideCount(event.getGuideCount() - 1);
            eventRepository.save(event); // Persist the changes
        } else {
            throw new IllegalArgumentException("Guide count cannot be less than 1");
        }
    }

    @Transactional
    public void removeGuideFromTour(Long eventId, Long guideId) {

        // Fetch the tour and guide from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found"));
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Check if the guide is assigned to this tour
        if (!event.getGuides().contains(guide)) {
            throw new IllegalArgumentException("This guide is not assigned to this tour.");
        }

        // Remove the guide from the tour's guide list
        event.getGuides().remove(guide);

        // Save the updated tour (cascade will handle guide changes)
        eventRepository.save(event);

        System.out.println("Guide removed from tour: " + guide.getFirstName() + " " + guide.getLastName());
    }


    public void approveFair(Long id) {
    }

    public void rejectFair(Long id) {
    }
}


