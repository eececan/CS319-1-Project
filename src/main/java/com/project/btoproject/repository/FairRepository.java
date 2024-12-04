package com.project.btoproject.repository;

import com.project.btoproject.model.Fair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface FairRepository extends JpaRepository<Fair, Long> {

    @Query("SELECT MAX(f.applicationTimeStamp) FROM Fair f")
    Date findLatestApplicationTimeStamp();
}