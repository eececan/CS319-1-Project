package com.project.btoproject.service;

import com.project.btoproject.model.Director;

import java.util.Optional;

public interface IDirectorService {
    Optional<Director> getDirectorById(Long id);
}
