package com.project.btoproject.service;

import com.project.btoproject.model.Fair;
import com.project.btoproject.repository.FairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FairService {
    private final FairRepository fairRepository;

    @Autowired
    public FairService(FairRepository fairRepository) {
        this.fairRepository = fairRepository;
    }

    public Date findLatestApplicationTimeStamp() {
        return fairRepository.findLatestApplicationTimeStamp();
    }

    public void saveAll(List<Fair> fairs) {
        fairRepository.saveAll(fairs);
    }
}
