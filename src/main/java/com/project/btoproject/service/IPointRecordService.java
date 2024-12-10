package com.project.btoproject.service;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.PointRecord;

import java.util.List;

public interface IPointRecordService {
    void saveRecord(PointRecord record);
    PointRecord findRecordById(Long id);
    List<PointRecord> getPointRecordsByGuide(Guide guide);
    List<PointRecord> findAllRecords();

}
