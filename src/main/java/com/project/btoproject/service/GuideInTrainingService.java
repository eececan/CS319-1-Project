package com.project.btoproject.service;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import com.project.btoproject.model.GuideInTraining;
import com.project.btoproject.repository.IGuideInTrainingRepository;
import com.project.btoproject.repository.IGuideRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuideInTrainingService implements IGuideInTrainingService {

    private final IGuideInTrainingRepository guideInTrainingRepository;
    private final IGuideRepository guideRepository;

    @Override
    public void saveGuideInTraining(GuideInTraining guideInTraining) {
        guideInTrainingRepository.save(guideInTraining);
    }

    @Override
    public List<Event> seeAssignedEvents(Long id) {
        Optional<GuideInTraining> guideInTraining = guideInTrainingRepository.findById(id);
        if(guideInTraining.isPresent()) {
            GuideInTraining gt = guideInTraining.get();
            List<Event> events = gt.getEvents();
            return events;
        }
        else {
            throw new IllegalArgumentException("Guide in training is not found with id: " + id);
        }
    }

    @Override
    public void convertToGuide(Long id) {
        Guide guide = new Guide();
        Optional<GuideInTraining> guideInTraining = guideInTrainingRepository.findById(id);
        if(guideInTraining.isPresent() && guideInTraining.get().isTrainingComplete()) {
            GuideInTraining gt = guideInTraining.get();

            guide.setId(gt.getId());
            guide.setFirstName(gt.getFirstName());
            guide.setLastName(gt.getLastName());
            guide.setPassword(gt.getPassword());
            guide.setPhoneNumber(gt.getPhoneNumber());
            guide.setEmail(gt.getEmail());
            guide.setPicture(gt.getPicture());
            guide.setStartDate(gt.getStartDate());
            guide.setDescription(gt.getDescription());
            guide.setDepartment(gt.getDepartment());
            guide.setGrade(gt.getGrade());
            guide.setEvents(gt.getEvents());
            guide.setSchedule(gt.getSchedule());
            guide.setPoints(null);
            guide.setTasks(gt.getTasks());

            guideInTrainingRepository.deleteById(id);
            guideRepository.save(guide);
        }
        else {
            throw new IllegalArgumentException("Guide in training is not found with id: " + id);
        }
    }

    @Override
    public GuideInTraining getGuideInTrainingById(Long id) {
        Optional<GuideInTraining> guideInTraining = guideInTrainingRepository.findById(id);
        if (guideInTraining.isPresent()) {
            return guideInTraining.get();
        } else {
            throw new EntityNotFoundException("GuideInTraining with ID " + id + " not found.");
        }
    }

}
