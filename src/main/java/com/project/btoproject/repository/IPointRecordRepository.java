package com.project.btoproject.repository;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPointRecordRepository extends JpaRepository<PointRecord, Long>  {
    PointRecord getPointRecordById(Long id);
    List<PointRecord> getPointRecordsByGuideId(Long guideId);
}
