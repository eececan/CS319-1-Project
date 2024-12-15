package com.project.btoproject;

import com.project.btoproject.enums.NotificationType;
import com.project.btoproject.model.Notification;
import com.project.btoproject.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;

    @Test
    void testNotificationCreation() {
        // Create a test notification
        notificationService.createNotification(
                1L,
                "Test notification",
                NotificationType.NEW_TOUR_APPLICATION

        );

        // Verify notification was created
        List<Notification> notifications = notificationService.getUnreadNotifications(1L);
        assertFalse(notifications.isEmpty());
        assertEquals("Test notification", notifications.get(0).getMessage());
    }

    @Test
    void testMarkAsRead() {
        // Create notification
        notificationService.createNotification(
                1L,
                "Test notification",
                NotificationType.NEW_TOUR_APPLICATION

        );

        // Get notification and mark as
    }
}