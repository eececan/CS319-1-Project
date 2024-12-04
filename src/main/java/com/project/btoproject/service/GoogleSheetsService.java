package com.project.btoproject.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.Fair;
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

    // Use TourService for database operations
    private FairService fairService;

    // Google Sheet attributes
    private static final String FAIR_SPREADSHEET_ID = "1E6i3VIJuqoVcsQ4iaZkipwhNt6f9LlcHenf1YRLGYmU";
    private static final String FAIR_SPREADSHEET_RANGE = "A2:L";

    @Autowired
    public GoogleSheetsService(TourService tourService, FairService fairService, SchoolService schoolService, SchoolCounselorService schoolCounselorService) {
        this.tourService = tourService;
        this.fairService = fairService;
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

    public List<List<Object>> fetchTourData() throws IOException {
        // Call the Sheets API to retrieve values and return the rows as a list of lists
        ValueRange response = sheetsService.spreadsheets().values()
                .get(TOUR_SPREADSHEET_ID, TOUR_SPREADSHEET_RANGE)
                .execute();

        // Handle null or empty data
        if (response.getValues() == null || response.getValues().isEmpty()) {
            System.out.println("No data found in the Google Sheet.");
            return Collections.emptyList();
        }

        return response.getValues();
    }

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
        School school = schoolService.findOrCreateSchool(schoolName, city, "");
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

        // In case counselor already exists by name and school, but he changed his phone and email, update those
        schoolCounselor.setPhoneNumber(counselorPhone);
        schoolCounselor.setEmail(counselorEmail);
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

    public List<List<Object>> fetchFairData() throws IOException {
        // Call the Sheets API to retrieve values and return the rows as a list of lists
        ValueRange response = sheetsService.spreadsheets().values()
                .get(FAIR_SPREADSHEET_ID, FAIR_SPREADSHEET_RANGE)
                .execute();

        // Handle null or empty data
        if (response.getValues() == null || response.getValues().isEmpty()) {
            System.out.println("No data found in the Google Sheet.");
            return Collections.emptyList();
        }

        return response.getValues();
    }

    public void saveNewFairs() throws IOException {
        // Get the latest applicationTimeStamp from the database (implement in FairService)
        Date latestTimestamp = fairService.findLatestApplicationTimeStamp();

        // Fetch all rows from Google Sheets
        List<List<Object>> rows = fetchFairData();

        // Prepare a list for new Fair objects
        List<Fair> newFairs = new ArrayList<>();

        // Iterate through the rows and filter only new applications
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

                    // Map the row to a Fair object
                    Fair fair = mapRowToFair(row);

                    // Add the new fair to the list
                    newFairs.add(fair);
                } catch (ParseException e) {
                    // Handle date parsing issues
                    System.err.println("Error parsing timestamp for row: " + row);
                    e.printStackTrace();
                }
            }
        }

        // Save all new fairs to the database
        if (!newFairs.isEmpty()) {
            fairService.saveAll(newFairs);
            System.out.println("Saved " + newFairs.size() + " new fairs to the database.");
        } else {
            System.out.println("No new fairs to save.");
        }
    }

    private Fair mapRowToFair(List<Object> row) throws ParseException {
        Fair fair = new Fair();

        // Parse the timestamp from Column A
        String timestampString = row.get(0).toString(); // Column A: Timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date applicationTimeStamp = dateFormat.parse(timestampString);
        fair.setApplicationTimeStamp(applicationTimeStamp);

        // Parse school name and address (Column B: School Name, Column C: Address, Column D: City)
        String schoolName = row.get(1).toString();
        String schoolAddress = row.get(2).toString();
        String city = row.get(3).toString();
        School school = schoolService.findOrCreateSchool(schoolName, city, schoolAddress);
        fair.setSchool(school);

        // Do the same as the tour
        // Parse contact person details (Columns E-H)
        String counselorName = row.get(4).toString(); // Column E: Counselor Name
        String counselorRole = row.get(5).toString(); // Column F: Counselor Role
        String counselorPhone = row.get(6).toString(); // Column G: Counselor Phone
        String counselorEmail = row.get(7).toString(); // Column H: Counselor Email

        SchoolCounselor schoolCounselor = schoolCounselorService.findOrCreateCounselor(
                counselorName, counselorRole, counselorPhone, counselorEmail, school);
        fair.setSchoolCounselor(schoolCounselor);

        // Parse preferred date and time (Columns I, J)
        String preferredDateString = row.get(8).toString();
        SimpleDateFormat preferredDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date preferredDate = preferredDateFormat.parse(preferredDateString);
        fair.setDate(preferredDate);

        String preferredTime = row.get(9).toString();
        fair.setHour(preferredTime);

        // Parse estimated student count (Column K)
        fair.setPeopleCount(Integer.parseInt(row.get(10).toString()));

        // Parse event details or additional notes (Column L)
        fair.setVisitorNotes(row.get(11).toString());


        return fair;
    }

}
