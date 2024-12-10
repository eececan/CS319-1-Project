package com.project.btoproject.model;

import com.project.btoproject.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tasks")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @Column(name = "task_name", nullable = false)
    private String taskName;

    @Column(name = "task_description", nullable = true)
    private String taskDescription;

    @Column(name = "task_deadline", nullable = true)
    private Date taskDeadline;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //completed or uncompleted
    @Column(name="state",nullable = true)
    private boolean state;
}
