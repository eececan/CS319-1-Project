package com.project.btoproject.repository;

import com.project.btoproject.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGuideRepository extends JpaRepository<Guide, Long> {
}
