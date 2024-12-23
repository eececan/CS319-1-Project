package com.project.btoproject.controller;

import com.project.btoproject.model.Notification;
import com.project.btoproject.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication auth) {
        try {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());
            List<Notification> notifications = notificationService.getUnreadNotifications(userId);
            System.out.println("Found " + notifications.size() + " unread notifications for user " + userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.err.println("Error getting notifications: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(Authentication auth) {
        try {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Long userId = Long.parseLong(userDetails.getUsername());
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error marking all notifications as read: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
