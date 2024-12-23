package com.project.btoproject.model;

import com.project.btoproject.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String message;
    private LocalDateTime timestamp;
    private boolean read;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}