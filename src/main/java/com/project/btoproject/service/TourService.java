package com.project.btoproject.service;

import com.project.btoproject.model.Tour;
import com.project.btoproject.model.TourInfo;
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
    public void changeTourInformation(Tour t, TourInfo tourInfo) {
        Optional<Tour> tourOptional = tourRepository.findById(t.getId())
                .filter(event -> event instanceof Tour)
                .map(event -> (Tour) event);

        if (tourOptional.isPresent()) {
            Tour tour = tourOptional.get();
            tour.setTourInformation(tourInfo);
            tourRepository.save(tour);
        } else {
            throw new IllegalArgumentException("Tour not found.");
        }
    }
}

