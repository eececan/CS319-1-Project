package com.project.btoproject.service;


import com.project.btoproject.model.Notification;
import com.project.btoproject.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotification(Long userId, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();
        notificationRepository.save(notification);
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
}
