package com.project.btoproject.service;

import com.project.btoproject.model.HeadSecretary;

import java.util.Optional;

public interface IHeadSecretaryService {
    Optional<HeadSecretary> getHeadSecretaryById(Long id);
}
