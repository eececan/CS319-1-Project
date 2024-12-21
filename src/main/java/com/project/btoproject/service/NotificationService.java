package com.project.btoproject.service;


import com.project.btoproject.enums.NotificationType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final GuideService guideService;
    private final AdvisorService advisorService;
    private final HeadSecretaryService headSecretaryService;
    private final CoordinatorService coordinatorService;
    private final DirectorService directorService;
    private final UserService userService;
    private final AllUsersService allUsersService;
    public void createNotification(Long userId, String message) {
        System.out.println("Creating notification for user " + userId + ": " + message);
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        System.out.println("Notification created with ID: " + saved.getId());
    }
    public void sendNotification(User user, String message) {
        createNotification(user.getId(), message);
    }
    public void notifyNewTourApplication(Tour tour) {
        String message = String.format("New tour application from %s for date %s",
                tour.getSchool().getName(),
                tour.getDate().toString());

        // Notify all advisors
        List<Advisor> advisors = advisorService.getAllAdvisors();
        for (Advisor advisor : advisors) {
            createNotification(advisor.getId(), message);
        }


    }
    public void notifyNewIndividualTourApplication(IndividualTour tour) {
        // Notify all advisors
        String advisorMessage = String.format("New individual tour application received from %s for date %s",
                tour.getStudent().getName(),
                new SimpleDateFormat("dd/MM/yyyy").format(tour.getDate()));

        List<Advisor> advisors = advisorService.getAllAdvisors();
        for (Advisor advisor : advisors) {
            createNotification(advisor.getId(), advisorMessage);
        }
    }

    public void notifyNewFairApplication(Fair fair) {
        // Notify coordinator and director
        String message = String.format("New fair application received from %s for date %s",
                fair.getSchool().getName(),
                new SimpleDateFormat("dd/MM/yyyy").format(fair.getDate()));

        // Notify coordinator
        List<Coordinator> coordinators = coordinatorService.getAllCoordinators();
        if (coordinators != null) {
            for (Coordinator coordinator : coordinators) {
                createNotification(coordinator.getId(), message);
            }

        }

        // Notify director
        List<User> users = allUsersService.getAllUsers();
        Optional<User> director = users.stream()
                .filter(user -> user instanceof Director)
                .findFirst();

        if (director.isPresent()) {
            createNotification(director.get().getId(), message);
        }
    }
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    public void notifyGuideAssigned(Event event, Guide guide) {
        String eventType;
        String message;
        if (event instanceof Tour) {
            eventType = "tour";
            Tour tour = (Tour) event;
            message = String.format("You have been assigned to %s for %s on %s",
                    eventType,
                    tour.getSchool().getName(),
                    tour.getDate().toString());
        } else if (event instanceof Fair) {
            eventType = "fair";
            Fair fair = (Fair) event;
            message = String.format("You have been assigned to %s for %s on %s",
                    eventType,
                    fair.getSchool().getName(),
                    fair.getDate().toString());
        } else {
            throw new IllegalArgumentException("Event must be either a Tour or a Fair");
        }
        createNotification(guide.getId(), message);
    }

    public void notifyEventApproved(Event event) {
        String eventType;
        String message;

        if (event instanceof Tour) {
            eventType = "Tour";
            Tour tour = (Tour) event;
            message = String.format("%s for %s on %s has been approved",
                    eventType,
                    tour.getSchool().getName(),
                    new SimpleDateFormat("dd/MM/yyyy").format(tour.getDate()));
            if (tour.getStatus() == Status.UPCOMING_TOUR) {
                String newMessage = message+ " You can assign yourself";
                for (Guide guide : guideService.getAllGuides()) {

                    createNotification(guide.getId(), newMessage);
                }
                String newMessageAd = message+ " You can assign guides";
                for (Advisor advisor : advisorService.getAllAdvisors()) {

                    createNotification(advisor.getId(), newMessageAd);
                }
            }

            if (tour.getStatus() == Status.BTO_ACCEPTED) {
                notifyHeadSecretaryTourApproved(tour);
                
            }
        } else if (event instanceof Fair) {
            eventType = "Fair";
            Fair fair = (Fair) event;
            message = String.format("%s for %s on %s has been approved",
                    eventType,
                    fair.getSchool().getName(),
                    new SimpleDateFormat("dd/MM/yyyy").format(fair.getDate()));

            if (fair.getStatus() == Status.UPCOMING_FAIR) {
                String newMessage = message+ " You can assign yourself";
                for (Guide guide : guideService.getAllGuides()) {

                    createNotification(guide.getId(), newMessage);
                }
            }

        } else {
            throw new IllegalArgumentException("Event must be either a Tour or a Fair");
        }
    }

    public void notifyHeadSecretaryTourApproved(Tour tour) {
        // Notify head secretary about advisor's approval
        String message = String.format("Tour application from %s for date %s has been approved by advisor",
                tour.getSchool().getName(),
                new SimpleDateFormat("dd/MM/yyyy").format(tour.getDate()));
        List<User> users = allUsersService.getAllUsers();
        Optional<User> headSecretary = users.stream()
                .filter(user -> user instanceof HeadSecretary)
                .findFirst();
        if (headSecretary.isPresent()) {
            createNotification(headSecretary.get().getId(), message);
        } else {
            throw new RuntimeException("Head Secretary not found");
        }

    }


}
