package com.project.btoproject;

import com.project.btoproject.service.GoogleSheetsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
public class GoogleSheetsIntegrationTest {

    @Autowired
    private GoogleSheetsService googleSheetsService;


    @Test
    public void testSaveNewTours() {
        String spreadsheetId = "1FHzTMk7yby8Y2eKa2uNWtnTWs1s4tCnItCmvAnBQLwc";
        String range = "A2:K"; // Adjust the range based on your sheet

        try {
            googleSheetsService.saveNewTours();
            System.out.println("New tours fetched and saved successfully if there are!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to save new tours: " + e.getMessage());
        }
    }

    @Test
    public void testSaveNewFairs() {
        String spreadsheetId = "1E6i3VIJuqoVcsQ4iaZkipwhNt6f9LlcHenf1YRLGYmU";
        String range = "A2:L";

        try {
            googleSheetsService.saveNewFairs();
            System.out.println("New fairs fetched and saved successfully if there are!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to save new fairs: " + e.getMessage());
        }
    }

    @Test
    public void testSaveNewIndividualTours() {
        String spreadsheetId = "1Z5is13p8dc2W3md4IbypdTzpmGUenpfcesNjOqw5wrg";
        String range = "A2:J"; // Adjust the range based on your sheet

        try {
            googleSheetsService.saveNewIndividualTours();
            System.out.println("New tours fetched and saved successfully if there are!");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to save new tours: " + e.getMessage());
        }
    }


}
