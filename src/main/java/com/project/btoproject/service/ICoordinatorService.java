package com.project.btoproject.service;

import com.project.btoproject.model.Coordinator;

public interface ICoordinatorService {
    void saveCoordinator(Coordinator coordinator);
    void deleteCoordinatorById(Long coordinatorId);
    Coordinator getCoordinatorById(Long CoordinatorId);
}
