package com.project.btoproject.service;

import com.project.btoproject.model.Coordinator;
import com.project.btoproject.repository.ICoordinatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordinatorService implements ICoordinatorService {

    private final ICoordinatorRepository coordinatorRepository;

    @Override
    public void saveCoordinator(Coordinator coordinator) {
        coordinatorRepository.save(coordinator);
    }

    @Override
    public void deleteCoordinatorById(Long coordinatorId) {
        coordinatorRepository.deleteById(coordinatorId);
    }

    @Override
    public Coordinator getCoordinatorById(Long CoordinatorId) {
        return coordinatorRepository.getCoordinatorById(CoordinatorId);
    }

    @Override
    public List<Coordinator> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }
}
