package com.project.btoproject.service;


import com.project.btoproject.enums.NotificationType;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final GuideService guideService;
    private final AdvisorService advisorService;
    private final UserService userService;
    public void createNotification(Long userId, String message,NotificationType type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();
        notificationRepository.save(notification);
    }
    public void notifyNewTourApplication(Tour tour) {
        String message = String.format("New tour application from %s for date %s",
                tour.getSchool().getName(),
                tour.getDate().toString());

        // Notify all advisors
        List<Advisor> advisors = advisorService.getAllAdvisors();
        for (Advisor advisor : advisors) {
            createNotification(advisor.getId(), message, NotificationType.NEW_TOUR_APPLICATION);
        }

        // Notify available guides
        List<Guide> guides = guideService.getAllGuides();
        for (Guide guide : guides) {
            createNotification(guide.getId(), message, NotificationType.NEW_TOUR_APPLICATION);
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
    public void notifyGuideAssigned(Tour tour, Guide guide) {
        String message = String.format("You have been assigned to tour for %s on %s",
                tour.getSchool().getName(),
                tour.getDate().toString());

        createNotification(guide.getId(), message, NotificationType.GUIDE_ASSIGNED);
    }
    public void notifyTourApproved(Tour tour) {
        String message = String.format("Tour for %s on %s has been approved",
                tour.getSchool().getName(),
                tour.getDate().toString());

        // Notify assigned guides
        for (Guide guide : tour.getGuides()) {
            createNotification(guide.getId(), message, NotificationType.TOUR_APPROVED);
        }
    }
}
