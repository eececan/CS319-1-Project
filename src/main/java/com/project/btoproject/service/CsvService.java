package com.project.btoproject.service;

import com.project.btoproject.model.HighSchoolForStatistics;
import com.project.btoproject.repository.HighSchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private HighSchoolRepository highSchoolRepository;

    public void saveCsvDataToDatabase() throws IOException {
        // Path to the CSV file in the resources folder
        ClassPathResource resource = new ClassPathResource("files/okullar_ve_ogrenciler_sehirler_son_sheets_at.csv");
        Path path = resource.getFile().toPath();

        // Read the CSV file
        BufferedReader reader = Files.newBufferedReader(path);
        String line;
        List<HighSchoolForStatistics> highSchools = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] values = line.split(";");
            if (values.length == 2) {
                System.out.println("In if " );
                HighSchoolForStatistics highSchool = new HighSchoolForStatistics();
                highSchool.setName(values[0]);
                highSchool.setStudentCount(Long.parseLong(values[1]));
                highSchools.add(highSchool);
                System.out.println("Parsed: " + highSchool.getName() + " - " + highSchool.getStudentCount());
            }
        }
        // Save all data in the database at once
        highSchoolRepository.saveAll(highSchools);

        reader.close();
    }
}
