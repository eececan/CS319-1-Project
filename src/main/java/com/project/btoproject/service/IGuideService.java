package com.project.btoproject.service;

import com.project.btoproject.model.*;

import java.util.List;

public interface IGuideService {
    void saveGuide(Guide guide);
    List<Event> seeAssignedEvents(Guide g);
    void selfAssignTour(Guide guide, Tour tour);
    void selfAssignIndividualTour(Guide guide, IndividualTour tour);
    int seeCurrentPoints(Guide g);
    Guide getGuideByName(String firstName, String lastName);
    Guide getGuideById(Long id);
    List<Guide> getAllGuides();
    void deleteGuide(Long guideId);
    List<Guide>getGuidesByDepartment(String department);

    /*
    guide related methods to be implemented

    void unassignGuideFromTour(Long tourId, Long guideId);
    List<Long> getGuideRankings();
    int getGuideRanking(Long guideId);
*/

}
