package com.project.btoproject.service;

import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.IEventRepository;
import com.project.btoproject.repository.IGuideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private IEventRepository eventRepository;
    private IGuideRepository guideRepository;
    private final NotificationService notificationService;
    @Autowired
    public EventService(IEventRepository eventRepository, IGuideRepository guideRepository, NotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.guideRepository = guideRepository;
        this.notificationService = notificationService;
    }
    public void approveFair(Long fairId) {
        Optional<Event> eventOptional = eventRepository.findById(fairId);
        if (eventOptional.isPresent() && eventOptional.get() instanceof Fair) {
            Fair fair = (Fair) eventOptional.get();
            if (!fair.getStatus().equals(Status.NEW_FAIR_APPLICATION)) {
                throw new IllegalStateException("Fair is not in a state to be approved.");
            }
            fair.setStatus(Status.UPCOMING_FAIR);
            eventRepository.save(fair);
        }

    else {
            throw new IllegalArgumentException("Fair not found with ID: " + fairId);
        }
    }

    public void rejectFair(Long fairId) {
        Optional<Event> eventOptional = eventRepository.findById(fairId);
        if (eventOptional.isPresent() && eventOptional.get() instanceof Fair) {
            Fair fair = (Fair) eventOptional.get();
            if (!fair.getStatus().equals(Status.NEW_FAIR_APPLICATION)) {
                throw new IllegalStateException("Fair is not in a state to be rejected.");
            }
            fair.setStatus(Status.REJECTED_FAIR);
            eventRepository.save(fair);
        }

        else {
            throw new IllegalArgumentException("Fair not found with ID: " + fairId);
        }
    }
    @Transactional
    public void cancelFair(Long fairId) {

        Optional<Event> eventOptional = eventRepository.findById(fairId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Fair) {
            Fair fair = (Fair) eventOptional.get();


            if (!fair.getStatus().equals(Status.UPCOMING_FAIR)) {
                throw new IllegalStateException("Fair is not in a state to be canceled.");
            }


            List<Guide> assignedGuides = fair.getGuides();
            for (Guide guide : assignedGuides) {
                guide.getEvents().remove(fair); // Remove the fair from the guide's events
                guideRepository.save(guide);   // Save the updated guide
            }


            fair.getGuides().clear();

            fair.setStatus(Status.CANCELED_FAIR);

            eventRepository.save(fair);
        } else {
            throw new IllegalArgumentException("Tour not found or invalid ID: " + fairId);
        }
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
            notificationService.notifyEventApproved(tour);
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

    @Transactional
    public void cancelTourBySecretary(Long tourId) {
        // Fetch the event and check if it is a tour
        Optional<Event> eventOptional = eventRepository.findById(tourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof Tour) {
            Tour tour = (Tour) eventOptional.get();

            // Ensure the tour is in a cancellable state
            if (!tour.getStatus().equals(Status.UPCOMING_TOUR)) {
                throw new IllegalStateException("Tour is not in a state to be canceled.");
            }

            // Remove the tour from each guide's event list
            List<Guide> assignedGuides = tour.getGuides();
            for (Guide guide : assignedGuides) {
                guide.getEvents().remove(tour); // Remove the tour from the guide's events
                guideRepository.save(guide);   // Save the updated guide
            }

            // Clear the tour's guide list
            tour.getGuides().clear();

            // Set the tour's status to CANCELED_TOUR
            tour.setStatus(Status.CANCELED_TOUR);

            // Save the updated tour
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

    public Date findLatestIndividualTourApplicationTimeStamp() {
        return eventRepository.findLatestIndividualTourApplicationTimeStamp();
    }

    public void saveAllTours(List<Tour> tours) {
        eventRepository.saveAll(tours); // Save tours as they are also events
    }

    public void saveAllFairs(List<Fair> fairs) {
        eventRepository.saveAll(fairs); // Save fairs as they are also events
    }

    public void saveAllIndividualTours(List<IndividualTour> individualTours) {
        eventRepository.saveAll(individualTours); // Save Individual Tours as they are also events
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
                Status.UPCOMING_TOUR
        );
        return eventRepository.findToursByStatuses(applicationStatuses);
    }
    public List<Fair> getFairApplications() {
        List<Status> applicationStatuses = List.of(
                Status.NEW_FAIR_APPLICATION,
                Status.UPCOMING_FAIR,
                Status.REJECTED_FAIR
        );
        return eventRepository.findFairsByStatuses(applicationStatuses);
    }


    public List<IndividualTour> getIndividualTourApplications() {
        List<Status> applicationStatuses = List.of(
                Status.NEW_INDIVIDUAL_TOUR_APPLICATION,
                Status.REJECTED_INDIVIDUAL_TOUR_APPLICATION,
                Status.UPCOMING_INDIVIDUAL_TOUR
        );
        return eventRepository.findIndividualToursByStatuses(applicationStatuses);
    }

    public List<Tour> getTours() {
        List<Status> tourStatuses = List.of(
                Status.UPCOMING_TOUR,
                Status.CANCELED_TOUR,
                Status.COMPLETED_TOUR
        );
        return eventRepository.findToursByStatuses(tourStatuses);
    }

    public List<IndividualTour> getIndividualTours() {
        List<Status> tourStatuses = List.of(
                Status.UPCOMING_INDIVIDUAL_TOUR,
                Status.COMPLETED_INDIVIDUAL_TOUR,
                Status.CANCELED_INDIVIDUAL_TOUR
        );
        return eventRepository.findIndividualToursByStatuses(tourStatuses);
    }
    public List<Fair> getFairs() {
        List<Status> tourStatuses = List.of(
                Status.UPCOMING_FAIR,
                Status.COMPLETED_FAIR,
                Status.CANCELED_FAIR
        );
        return eventRepository.findFairsByStatuses(tourStatuses);
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
        System.out.println("Tours of the guide: " + guide.getEvents());
        eventRepository.save(event); // Save the updated tour
        System.out.println("Tours of the guide: " + guide.getEvents());
        notificationService.notifyGuideAssigned((Tour)event, guide);
    }

    @Transactional
    public void assignGuideToIndividualTour(Long individualTourId, Long guideId) {
        // Fetch the individual tour and guide from the database
        IndividualTour individualTour = eventRepository.findById(individualTourId)
                .filter(event -> event instanceof IndividualTour)
                .map(event -> (IndividualTour) event)
                .orElseThrow(() -> new IllegalArgumentException("Individual Tour not found or invalid ID"));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Check if a guide is already assigned to this individual tour
        if (individualTour.getGuides() != null && !individualTour.getGuides().isEmpty()) {
            Guide existingGuide = individualTour.getGuides().get(0); // Get the assigned guide
            if (existingGuide.getId().equals(guideId)) {
                throw new IllegalArgumentException("This guide is already assigned to this individual tour.");
            }
            // Clear the existing guide to allow reassignment
            individualTour.getGuides().clear();

        }

        // Check if the guide has another individual tour on the same date and hour
        boolean hasConflict = guide.getEvents().stream()
                .filter(event -> event instanceof IndividualTour)
                .anyMatch(existingTour -> existingTour.getDate().equals(individualTour.getDate())
                        && ((IndividualTour) existingTour).getHour().equals(individualTour.getHour()));

        if (hasConflict) {
            throw new IllegalArgumentException("This guide is already assigned to another individual tour at the same time.");
        }

        // Assign the guide to the individual tour (add to the guides list)
        individualTour.getGuides().add(guide);

        // Save the updated individual tour
        eventRepository.save(individualTour);
    }

    @Transactional
    public void removeGuideFromIndividualTour(Long individualTourId, Long guideId) {
        // Fetch the individual tour and guide from the database
        Event event = eventRepository.findById(individualTourId)
                .orElseThrow(() -> new IllegalArgumentException("Individual Tour not found"));
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Ensure the event is an IndividualTour
        if (!(event instanceof IndividualTour)) {
            throw new IllegalArgumentException("The specified event is not an Individual Tour.");
        }

        IndividualTour individualTour = (IndividualTour) event;

        // Check if the guide is assigned to this individual tour
        if (!individualTour.getGuides().contains(guide)) {
            throw new IllegalArgumentException("This guide is not assigned to this Individual Tour.");
        }

        // Remove the guide from the tour's guide list
        individualTour.getGuides().remove(guide);

        // Save the updated individual tour
        eventRepository.save(individualTour);
    }

    @Transactional
    public void cancelIndividualTour(Long individualTourId) {
        // Fetch the event and check if it is an individual tour
        Optional<Event> eventOptional = eventRepository.findById(individualTourId);

        if (eventOptional.isPresent() && eventOptional.get() instanceof IndividualTour) {
            IndividualTour individualTour = (IndividualTour) eventOptional.get();

            // Ensure the individual tour is in a cancellable state
            if (!individualTour.getStatus().equals(Status.UPCOMING_INDIVIDUAL_TOUR)) {
                throw new IllegalStateException("Individual Tour is not in a state to be canceled.");
            }

            // Check if a guide is assigned
            List<Guide> assignedGuides = individualTour.getGuides();
            if (!assignedGuides.isEmpty()) {
                // Remove the individual tour from the assigned guide's events list
                Guide assignedGuide = assignedGuides.get(0); // Individual tours have only one guide
                assignedGuide.getEvents().remove(individualTour);
                guideRepository.save(assignedGuide); // Save the updated guide
            }

            // Clear the guide list for the individual tour
            individualTour.getGuides().clear();

            // Set the individual tour's status to CANCELED_INDIVIDUAL_TOUR
            individualTour.setStatus(Status.CANCELED_INDIVIDUAL_TOUR);

            // Save the updated individual tour
            eventRepository.save(individualTour);
        } else {
            throw new IllegalArgumentException("Individual Tour not found or invalid ID: " + individualTourId);
        }
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
    public void assignGuideToFair(Long eventId, Long guideId) {

        // Fetch the tour and guide from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Fair not found"));
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Check if the guide is already assigned to this tour
        if (event.getGuides().contains(guide)) {
            throw new IllegalArgumentException("This guide is already assigned to this fair.");
        }

        // Check if the guide has another tour on the same date and hour
        boolean hasConflict = guide.getEvents().stream()
                .anyMatch(existingFair -> existingFair.getDate().equals(event.getDate())
                        && ((Fair) existingFair).getHour().equals(((Fair) event).getHour()));
        if (hasConflict) {
            throw new IllegalArgumentException("This guide is already assigned to another fair at the same time.");
        }

        System.out.println("Guide assigned to fair: " + guide.getFirstName() + " " + guide.getLastName());
        // Add the guide to the tour
        (event.getGuides()).add(guide);
        System.out.println("Fairs of the guide: " + guide.getEvents());
        eventRepository.save(event); // Save the updated tour
        System.out.println("Fairs of the guide: " + guide.getEvents());
        notificationService.notifyGuideAssigned((Fair)event, guide);
    }
    @Transactional
    public void increaseGuideCountFair(Long fairId) {
        // Fetch the tour from the database
        Event event = eventRepository.findById(fairId)
                .orElseThrow(() -> new IllegalArgumentException("Tour not found"));

        // Ensure the event is a tour
        if (!(event instanceof Fair)) {
            throw new IllegalArgumentException("The specified event is not a tour.");
        }

        Fair fair = (Fair) event;

        // Check the current guide count
        if (fair .getGuideCount() >= 3) {
            throw new IllegalStateException("The maximum number of guides (3) is already assigned.");
        }

        // Increment the guide count
        fair.setGuideCount(fair.getGuideCount() + 1);

        // Save the updated tour to the database
        eventRepository.save(fair);
    }
    @Transactional
    public void removeGuideFromFair(Long eventId, Long guideId) {

        // Fetch the event and guide from the database
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Fair not found"));
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("Guide not found"));

        // Check if the guide is assigned to this tour
        if (!event.getGuides().contains(guide)) {
            throw new IllegalArgumentException("This guide is not assigned to this fair.");
        }

        // Remove the guide from the tour's guide list
        event.getGuides().remove(guide);

        // Save the updated tour (cascade will handle guide changes)
        eventRepository.save(event);

        System.out.println("Guide removed from fair: " + guide.getFirstName() + " " + guide.getLastName());
    }

    @Transactional
    public void decreaseGuideCount(Long eventId) {
        // Fetch the tour from the database
        Event event = eventRepository.findById(eventId)
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

    public long getUpcomingEventsCount() {
        List<Fair> upcomingFairs = getAllFairs().stream()
                .filter(fair -> fair.getStatus() == Status.UPCOMING_FAIR)
                .collect(Collectors.toList());

        List<Tour> upcomingTours = getAllTours().stream()  // Get upcoming tours
                .filter(tour -> tour.getStatus() == Status.UPCOMING_TOUR)
                .collect(Collectors.toList());
        List<IndividualTour> upcomingToursInd = getAllIndividualTours().stream()  // Get upcoming tours
                .filter(individualTour -> individualTour.getStatus() == Status.UPCOMING_TOUR)
                .collect(Collectors.toList());

        return upcomingFairs.size() + upcomingTours.size() + upcomingToursInd.size();  // Sum up both
    }

    public void approveIndividualTour(Long individualTourId) {
        Optional<Event> eventOptional = eventRepository.findById(individualTourId);

        // Check if the event exists and is of type IndividualTour
        if (eventOptional.isPresent() && eventOptional.get() instanceof IndividualTour) {
            IndividualTour individualTour = (IndividualTour) eventOptional.get();

            // Ensure the current status is NEW_INDIVIDUAL_TOUR_APPLICATION
            if (!individualTour.getStatus().equals(Status.NEW_INDIVIDUAL_TOUR_APPLICATION)) {
                throw new IllegalStateException("Individual Tour is not in a state to be approved.");
            }

            // Set the status to advisor approved
            individualTour.setStatus(Status.UPCOMING_INDIVIDUAL_TOUR); // Update status to approved
            eventRepository.save(individualTour); // Save changes to the repository
        } else {
            // Handle case where the event doesn't exist or is not an IndividualTour
            throw new IllegalArgumentException("Individual Tour not found or invalid ID: " + individualTourId);
        }
    }

    public void rejectIndividualTour(Long individualTourId) {
        Optional<Event> eventOptional = eventRepository.findById(individualTourId);

        // Check if the event exists and is of type IndividualTour
        if (eventOptional.isPresent() && eventOptional.get() instanceof IndividualTour) {
            IndividualTour individualTour = (IndividualTour) eventOptional.get();

            // Ensure the current status is NEW_INDIVIDUAL_TOUR_APPLICATION
            if (!individualTour.getStatus().equals(Status.NEW_INDIVIDUAL_TOUR_APPLICATION)) {
                throw new IllegalStateException("Individual Tour is not in a state to be rejected.");
            }

            // Set the status to REJECTED_INDIVIDUAL_TOUR_APPLICATION
            individualTour.setStatus(Status.REJECTED_INDIVIDUAL_TOUR_APPLICATION);
            eventRepository.save(individualTour); // Save changes to the repository
        } else {
            // Handle case where the event doesn't exist or is not an IndividualTour
            throw new IllegalArgumentException("Individual Tour not found or invalid ID: " + individualTourId);
        }
    }

    public void notifyGuidesForUpcomingEvents() {
        List<Fair> upcomingFairs = getAllFairs().stream()
                .filter(fair -> fair.getStatus() == Status.UPCOMING_FAIR)
                .collect(Collectors.toList());

        List<Tour> upcomingTours = getAllTours().stream()
                .filter(tour -> tour.getStatus() == Status.UPCOMING_TOUR)
                .collect(Collectors.toList());

        List<IndividualTour> upcomingIndividualTours = getAllIndividualTours().stream()
                .filter(individualTour -> individualTour.getStatus() == Status.UPCOMING_TOUR)
                .collect(Collectors.toList());

        Date now = new Date();

        for (Fair fair : upcomingFairs) {
            if (daysBetween(now, fair.getDate()) == 3) {
                notifyGuides(fair.getGuides(), fair);
            }
        }

        for (Tour tour : upcomingTours) {
            if (daysBetween(now, tour.getDate()) == 3) {
                notifyGuides(tour.getGuides(), tour);
            }
        }

        for (IndividualTour individualTour : upcomingIndividualTours) {
            if (daysBetween(now, individualTour.getDate()) == 3) {
                notifyGuides(individualTour.getGuides(), individualTour);
            }
        }
    }

    private void notifyGuides(List<Guide> guides, Event event) {
        for (Guide guide : guides) {
            String message = "Reminder: You have an upcoming " + event.getEventType() +
                    " on " + new SimpleDateFormat("dd/MM/yyyy").format(event.getDate()) +
                    ". Please prepare accordingly.";
            notificationService.sendNotification(guide, message);
        }
    }

    private long daysBetween(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public Page<Tour> getTourApplicationsPageable(int page, int size) {
        List<Status> applicationStatuses = List.of(
                Status.NEW_TOUR_APPLICATION,
                Status.BTO_ACCEPTED,
                Status.BTO_REJECTED,
                Status.UPCOMING_TOUR
        );
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findToursByStatusesPageable(applicationStatuses, pageable);
    }
    public Page<Tour> getToursPageable(int page, int size) {
        List<Status> tourStatuses = List.of(
                Status.COMPLETED_TOUR,
                Status.CANCELED_TOUR,
                Status.UPCOMING_TOUR
        );
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findToursByStatusesPageable(tourStatuses, pageable);
    }
}




