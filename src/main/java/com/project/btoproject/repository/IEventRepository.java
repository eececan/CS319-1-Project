package com.project.btoproject.repository;

import com.project.btoproject.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventRepository extends JpaRepository<Event, Long> {
}
