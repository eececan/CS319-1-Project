package com.project.btoproject.service;

import com.project.btoproject.model.Director;
import com.project.btoproject.repository.IDirectorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DirectorService implements IDirectorService {
    private final IDirectorRepository directorRepository;

    public DirectorService(IDirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        return directorRepository.findById(id);
    }
}
