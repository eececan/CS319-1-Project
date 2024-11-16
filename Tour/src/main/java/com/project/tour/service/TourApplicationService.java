package com.project.tour.service;
import com.project.tour.entity.TourApplicationEntity;
import com.project.tour.repository.TourApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourApplicationService {

    private final TourApplicationRepository repository;

    public TourApplicationService(TourApplicationRepository repository) {
        this.repository = repository;
    }

    public TourApplicationEntity createApplication(TourApplicationEntity application) {
        return repository.save(application);
    }

    public List<TourApplicationEntity> getAllApplications() {
        return repository.findAll();
    }

    public TourApplicationEntity getApplicationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public TourApplicationEntity updateApplication(Long id, TourApplicationEntity applicationDetails) {
        TourApplicationEntity application = repository.findById(id).orElse(null);
        if (application != null) {
            application.setInstitutionName(applicationDetails.getInstitutionName());
            application.setCity(applicationDetails.getCity());
            application.setDesiredVisitDate(applicationDetails.getDesiredVisitDate());
            application.setVisitTime(applicationDetails.getVisitTime());
            application.setNumberOfPeople(applicationDetails.getNumberOfPeople());
            application.setGroupLeaderName(applicationDetails.getGroupLeaderName());
            application.setGroupLeaderRole(applicationDetails.getGroupLeaderRole());
            application.setGroupLeaderPhone(applicationDetails.getGroupLeaderPhone());
            application.setGroupLeaderEmail(applicationDetails.getGroupLeaderEmail());
            application.setVisitorNotes(applicationDetails.getVisitorNotes());
            return repository.save(application);
        }
        return null;
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id);
    }
}
