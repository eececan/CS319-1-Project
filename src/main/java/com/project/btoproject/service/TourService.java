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

    /**
     * Saves a list of tours to the database.
     *
     * @param tours List of tours to save.
     */
    public void saveAll(List<Tour> tours) {
        tourRepository.saveAll(tours);
    }

    /**
     * Checks if a tour with the given timestamp already exists.
     *
     * @param applicationTimeStamp The timestamp to check.
     * @return True if the tour exists, otherwise false.
     */
    // Used previously, I kept it in case it is needed again
    public boolean existsByApplicationTimeStamp(Date applicationTimeStamp) {
        return tourRepository.existsByApplicationTimeStamp(applicationTimeStamp);
    }

    /**
     * Finds the most recent applicationTimeStamp from the database.
     *
     * @return The latest applicationTimeStamp, or null if no tours exist.
     */
    public Date findLatestApplicationTimeStamp() {
        return tourRepository.findLatestApplicationTimeStamp();
    }
}

