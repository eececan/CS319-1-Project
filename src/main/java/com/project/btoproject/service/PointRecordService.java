package com.project.btoproject.service;

import com.project.btoproject.model.Guide;
import com.project.btoproject.model.PointRecord;
import com.project.btoproject.repository.IPointRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointRecordService implements IPointRecordService {

    private final IPointRecordRepository pointRecordRepository;

    @Override
    public void saveRecord(PointRecord record) {
        pointRecordRepository.save(record);
    }

    @Override
    public PointRecord findRecordById(Long id) {
        return pointRecordRepository.getPointRecordById(id);
    }

    @Override
    public List<PointRecord> getPointRecordsByGuide(Long userId) {
        return pointRecordRepository.getPointRecordsByGuideId(userId);
    }

    @Override
    public List<PointRecord> findAllRecords() {
        return pointRecordRepository.findAll();
    }
}
