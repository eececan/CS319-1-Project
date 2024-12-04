package com.project.btoproject.service;

import com.project.btoproject.model.Tour;
import com.project.btoproject.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public void saveAll(List<Tour> tours) {
        tourRepository.saveAll(tours);
    }

    // Used previously, I kept it in case it is needed again
    public boolean existsByApplicationTimeStamp(Date applicationTimeStamp) {
        return tourRepository.existsByApplicationTimeStamp(applicationTimeStamp);
    }

    public Date findLatestApplicationTimeStamp() {
        return tourRepository.findLatestApplicationTimeStamp();
    }
}

