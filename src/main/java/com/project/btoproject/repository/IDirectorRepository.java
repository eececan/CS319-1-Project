package com.project.btoproject.repository;

import com.project.btoproject.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDirectorRepository extends JpaRepository<Director, Long> {
}
