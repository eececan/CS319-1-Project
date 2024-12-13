package com.project.btoproject.service;

import com.project.btoproject.model.Coordinator;

import java.util.List;

public interface ICoordinatorService {
    void saveCoordinator(Coordinator coordinator);
    void deleteCoordinatorById(Long coordinatorId);
    Coordinator getCoordinatorById(Long CoordinatorId);
    List<Coordinator> getAllCoordinators();
}
