package com.project.btoproject.service;

import com.project.btoproject.model.HeadSecretary;
import com.project.btoproject.repository.IHeadSecretaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HeadSecretaryService implements IHeadSecretaryService {

    private final IHeadSecretaryRepository repo;

    public HeadSecretaryService(IHeadSecretaryRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<HeadSecretary> getHeadSecretaryById(Long id) {
        return repo.findById(id);
    }
}
