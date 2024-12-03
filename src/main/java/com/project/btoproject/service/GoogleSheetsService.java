package com.project.btoproject.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.School;
import com.project.btoproject.model.SchoolCounselor;
import com.project.btoproject.model.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class GoogleSheetsService {

    // Sheets API client
    private Sheets sheetsService;

    // Path to the credentials JSON file
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    // Use TourService for database operations
    private TourService tourService;

    // Use SchoolService for database operations
    private SchoolService schoolService;

    // Use SchoolCounselorService for database operations
    private SchoolCounselorService schoolCounselorService;

    // Tour-specific Google Sheet attributes
    private static final String TOUR_SPREADSHEET_ID = "1FHzTMk7yby8Y2eKa2uNWtnTWs1s4tCnItCmvAnBQLwc";
    private static final String TOUR_SPREADSHEET_RANGE = "A2:K";

    /**
     * Constructor to inject TourService and initialize the Sheets API client.
     *
     * @param tourService The service to handle Tour-related operations.
     */
    @Autowired
    public GoogleSheetsService(TourService tourService, SchoolService schoolService, SchoolCounselorService schoolCounselorService) {
        this.tourService = tourService;
        this.schoolService = schoolService;
        this.schoolCounselorService = schoolCounselorService;

        // Initialize the Sheets API client
        try {
            this.sheetsService = initializeSheetsService();
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error initializing Google Sheets Service: " + e.getMessage());
            e.printStackTrace(); // For debugging
            throw new RuntimeException("Failed to initialize Google Sheets Service", e);
        }
    }

    /**
     * Initializes the Google Sheets API client using the provided credentials.
     *
     * @return Sheets API client instance.
     * @throws IOException               if the credentials file cannot be read.
     * @throws GeneralSecurityException  if a security error occurs during initialization.
     */
    private Sheets initializeSheetsService() throws IOException, GeneralSecurityException {
        // Load service account credentials from the JSON file
        try (FileInputStream credentialsStream = new FileInputStream(CREDENTIALS_FILE_PATH)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS_READONLY));

            // Build and return the Sheets API client
            return new Sheets.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("Bilkent University Project")
                    .build();
        }
    }

    /**
     * Fetch data for tours from the predefined Google Sheet and range.
     * @return List of rows, where each row is a list of cell values.
     * @throws IOException if the API call fails.
     */
    public List<List<Object>> fetchTourData() throws IOException {
        // Call the Sheets API to retrieve values and return the rows as a list of lists
        return sheetsService.spreadsheets().values()
                .get(TOUR_SPREADSHEET_ID, TOUR_SPREADSHEET_RANGE)
                .execute()
                .getValues();
    }

    /**
     * Fetches data from Google Sheets, maps it to Tour objects, and saves only new tours.
     *
     * @throws IOException If the Google Sheets API fails.
     */
    public void saveNewTours() throws IOException {
        // Get the latest applicationTimeStamp from the database
        Date latestTimestamp = tourService.findLatestApplicationTimeStamp();

        // Fetch all rows from Google Sheets
        List<List<Object>> rows = fetchTourData();

        // Step 3: Prepare a list for new Tour objects
        List<Tour> newTours = new ArrayList<>();

        // Step 4: Iterate through the rows and filter only new applications
        for (List<Object> row : rows) {
            if (!row.isEmpty()) {
                try {
                    // Parse the timestamp from the row
                    String timestampString = row.get(0).toString(); // Column A: Timestamp
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date rowTimestamp = dateFormat.parse(timestampString);

                    // Skip rows with timestamps earlier or equal to the latest processed timestamp
                    if (latestTimestamp != null && !rowTimestamp.after(latestTimestamp)) {
                        continue;
                    }

                    // Map the row to a Tour object
                    Tour tour = mapRowToTour(row);

                    // Add the new tour to the list
                    newTours.add(tour);
                } catch (ParseException e) {
                    // Handle date parsing issues
                    System.err.println("Error parsing timestamp for row: " + row);
                    e.printStackTrace();
                }
            }
        }

        // Save all new tours to the database
        if (!newTours.isEmpty()) {
            tourService.saveAll(newTours);
            System.out.println("Saved " + newTours.size() + " new tours to the database.");
        } else {
            System.out.println("No new tours to save.");
        }
    }

    /**
     * Maps a row from Google Sheets to a Tour object.
     *
     * @param row A single row of data from Google Sheets.
     * @return A Tour object populated with data from the row.
     * @throws ParseException If the timestamp format is invalid.
     */
    private Tour mapRowToTour(List<Object> row) throws ParseException {
        Tour tour = new Tour();

        // Parse the timestamp from Column A
        String timestampString = row.get(0).toString(); // Column A: Timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date applicationTimeStamp = dateFormat.parse(timestampString);
        tour.setApplicationTimeStamp(applicationTimeStamp);

        // Parse the school name from Column B and city from Column C
        String schoolName = row.get(1).toString();
        String city = row.get(2).toString();
        School school = schoolService.findOrCreateSchool(schoolName, city); // Delegate to SchoolService
        tour.setSchool(school);

        // Map Requested Visit Date (Column D)
        String visitDateString = row.get(3).toString();
        SimpleDateFormat requestedDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date requestedVisitDate = requestedDateFormat.parse(visitDateString);
        tour.setDate(requestedVisitDate);

        // Map Column E: Visit Hour
        String hourString = row.get(4).toString();
        Hour hour = mapTimeToHour(hourString);
        tour.setHour(hour);

        // Map Column F: Number of People
        tour.setPeopleCount(Integer.parseInt(row.get(5).toString()));

        // Map School Counselor (Columns G: Name, H: Role, I: Phone Number, J: Email, K: Comment)
        String counselorName = row.get(6).toString();
        String counselorRole = row.get(7).toString();
        String counselorPhone = row.get(8).toString();
        String counselorEmail = row.get(9).toString();

        SchoolCounselor schoolCounselor = schoolCounselorService.findOrCreateCounselor(
                counselorName, counselorRole, counselorPhone, counselorEmail, school);
        tour.setSchoolCounselor(schoolCounselor);

        // Map Column K: Contact Person Role
        tour.setVisitorNotes(row.get(10).toString());


        return tour;
    }

    // Helper function to convert time to the corresponding Hour enum
    private Hour mapTimeToHour(String time) {
        switch (time) {
            case "09:00":
                return Hour.NINE;
            case "11:00":
                return Hour.ELEVEN;
            case "13:30":
                return Hour.THIRTEEN_THIRTY;
            case "16:00":
                return Hour.SIXTEEN;
            default:
                throw new IllegalArgumentException("Invalid hour value: " + time);
        }
    }
}
