package com.project.tour.repository;
import com.project.tour.entity.TourApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourApplicationRepository extends JpaRepository<TourApplicationEntity, Long> {
}