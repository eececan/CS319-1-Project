package com.project.btoproject.service;

import com.project.btoproject.model.Event;
import com.project.btoproject.model.Guide;
import com.project.btoproject.model.GuideInTraining;

import java.util.List;

public interface IGuideInTrainingService {
    void saveGuideInTraining(GuideInTraining guideInTraining);
    List<Event> seeAssignedEvents(Long id);
    void convertToGuide(Long id);
    GuideInTraining getGuideInTrainingById(Long id);
}
