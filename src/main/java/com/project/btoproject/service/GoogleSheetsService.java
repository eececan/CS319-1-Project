package com.project.btoproject.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.project.btoproject.enums.Hour;
import com.project.btoproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final NotificationService notificationService;
    // Sheets API client
    private Sheets sheetsService;

    // Path to the credentials JSON file
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";

    // Services for database operations
    private final EventService eventService;
    private SchoolService schoolService;
    private SchoolCounselorService schoolCounselorService;
    private StudentService studentService;

    // Tour-specific Google Sheet attributes
    private static final String TOUR_SPREADSHEET_ID = "1FHzTMk7yby8Y2eKa2uNWtnTWs1s4tCnItCmvAnBQLwc";
    private static final String TOUR_SPREADSHEET_RANGE = "A2:K";

    // Google Sheet attributes
    private static final String FAIR_SPREADSHEET_ID = "1E6i3VIJuqoVcsQ4iaZkipwhNt6f9LlcHenf1YRLGYmU";
    private static final String FAIR_SPREADSHEET_RANGE = "A2:L";

    // Google Sheet attributes
    private static final String INDIVIDUAL_TOUR_SPREADSHEET_ID = "1Z5is13p8dc2W3md4IbypdTzpmGUenpfcesNjOqw5wrg";
    private static final String INDIVIDUAL_TOUR_SPREADSHEET_RANGE = "A2:J";

    /**
     * Service for interacting with Google Sheets to fetch event data.
     * This service integrates with multiple dependent services such as
     * SchoolService, SchoolCounselorService, StudentService, and EventService
     * to process and validate fetched data.
     */
    @Autowired
    public GoogleSheetsService(SchoolService schoolService, SchoolCounselorService schoolCounselorService, StudentService studentService, EventService eventService, NotificationService notificationService) {
        this.schoolService = schoolService;
        this.schoolCounselorService = schoolCounselorService;
        this.studentService = studentService;

        // Initialize the Sheets API client
        try {
            this.sheetsService = initializeSheetsService();
        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error initializing Google Sheets Service: " + e.getMessage());
            e.printStackTrace(); // For debugging
            throw new RuntimeException("Failed to initialize Google Sheets Service", e);
        }
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    /**
     * Initializes the Google Sheets API client.
     * This method configures authentication and sets up the service for interacting
     * with Google Sheets.
     *
     * @return An authorized instance of the Google Sheets service.
     * @throws IOException If an I/O error occurs during initialization.
     * @throws GeneralSecurityException If a security exception occurs during initialization.
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
     * Periodically fetches and saves new tours.
     * This method is scheduled to run every 10 seconds. It calls the {@link #saveNewTours()}
     * method to fetch data from the Google Sheet and save it into the system.
     * Any errors encountered during the process are logged to the console.
     */
    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void fetchAndSaveNewTours() {
        try {
            saveNewTours();
            System.out.println("Scheduled task: Tours fetched and saved.");
        } catch (IOException e) {
            System.err.println("Error fetching and saving tours: " + e.getMessage());
        }
    }

    /**
     * Fetches tour data from the Google Sheet.
     * This method calls the Google Sheets API to retrieve data from the specified
     * spreadsheet and range, and returns the rows as a list of lists.
     *
     * @return A list of rows from the Google Sheet, where each row is represented
     *         as a list of objects. Returns an empty list if no data is found.
     * @throws IOException If an error occurs while communicating with the Google Sheets API.
     */
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

    /**
     * Saves new tours by fetching data from the Google Sheet and comparing timestamps
     * to ensure only new entries are processed and stored in the database.
     *
     * @throws IOException If an error occurs while fetching data from Google Sheets.
     */
    public void saveNewTours() throws IOException {
        // Get the latest applicationTimeStamp from the database
        Date latestTimestamp = eventService.findLatestTourApplicationTimeStamp();

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
                    notificationService.notifyNewTourApplication(tour);
                } catch (ParseException e) {
                    // Handle date parsing issues
                    System.err.println("Error parsing timestamp for row: " + row);
                    e.printStackTrace();
                }
            }
        }

        // Save all new tours to the database
        if (!newTours.isEmpty()) {
            eventService.saveAllTours(newTours);
            System.out.println("Saved " + newTours.size() + " new tours to the database.");
        } else {
            System.out.println("No new tours to save.");
        }
    }

    /**
     * Maps a row of data from the Google Sheet to a {@link Tour} object.
     *
     * <p>This method parses and processes various fields in the row to construct
     * a {@link Tour} object with the following mappings:
     * <ul>
     *   <li>Column A: Application timestamp (parsed into a {@link Date}).</li>
     *   <li>Columns B and C: School name and city (mapped to a {@link School}).</li>
     *   <li>Column D: Requested visit date (parsed into a {@link Date}).</li>
     *   <li>Column E: Visit hour (converted to an {@link Hour} object).</li>
     *   <li>Column F: Number of people and calculated guide count.</li>
     *   <li>Columns G-J: School counselor details (name, role, phone, email).</li>
     *   <li>Column K: Visitor notes (stored as a string).</li>
     * </ul>
     *
     * <p>The method validates and ensures proper mapping for all required fields.
     * If a school or counselor does not exist, it creates them using the
     * appropriate service methods.</p>
     *
     * <p>Edge cases handled include:</p>
     * <ul>
     *   <li>Limiting the maximum number of guides to 3.</li>
     *   <li>Ensuring counselor details (phone and email) are updated if they change.</li>
     *   <li>Throwing a {@link ParseException} for invalid date or time formats.</li>
     * </ul>
     *
     * @param row A list of objects representing a row of data from the Google Sheet.
     *            The columns should correspond to the expected structure.
     * @return A {@link Tour} object constructed from the row data.
     * @throws ParseException If any date or time field fails to parse.
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
        int guideCount = (int) Math.ceil(Integer.parseInt(row.get(5).toString()) / 50.0); // Calculate guide count
        if(guideCount > 3)  guideCount = 3; // Limit guide count to 3
        tour.setGuideCount(guideCount);

        // Map School Counselor (Columns G: Name, H: Role, I: Phone Number, J: Email)
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

    /**
     * Periodically fetches and saves new fairs.
     * This method is scheduled to run every 10 seconds. It calls the {@link #saveNewFairs()}
     * method to fetch data from the Google Sheet and save it into the system.
     * Any errors encountered during the process are logged to the console.
     */
    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void fetchAndSaveNewFairs() {
        try {
            saveNewFairs();
            System.out.println("Scheduled task: Fairs fetched and saved.");
        } catch (IOException e) {
            System.err.println("Error fetching and saving fairs: " + e.getMessage());
        }
    }

    /**
     * Fetches fair data from the Google Sheet.
     * This method calls the Google Sheets API to retrieve data from the specified
     * spreadsheet and range, and returns the rows as a list of lists.
     *
     * @return A list of rows from the Google Sheet, where each row is represented
     *         as a list of objects. Returns an empty list if no data is found.
     * @throws IOException If an error occurs while communicating with the Google Sheets API.
     */
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

    /**
     * Saves new fairs by fetching data from the Google Sheet and comparing timestamps
     * to ensure only new entries are processed and stored in the database.
     *
     * @throws IOException If an error occurs while fetching data from Google Sheets.
     */
    public void saveNewFairs() throws IOException {
        // Get the latest applicationTimeStamp from the database (implement in FairService)
        Date latestTimestamp = eventService.findLatestFairApplicationTimeStamp();

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
                    notificationService.notifyNewFairApplication(fair);
                } catch (ParseException e) {
                    // Handle date parsing issues
                    System.err.println("Error parsing timestamp for row: " + row);
                    e.printStackTrace();
                }
            }
        }

        // Save all new fairs to the database
        if (!newFairs.isEmpty()) {
            eventService.saveAllFairs(newFairs);
            System.out.println("Saved " + newFairs.size() + " new fairs to the database.");
        } else {
            System.out.println("No new fairs to save.");
        }
    }

    /**
     * Maps a row of data from the Google Sheet to a {@link Fair} object.
     *
     * <p>This method parses and processes various fields in the row to construct
     * a {@link Fair} object with the following mappings:
     * <ul>
     *   <li>Column A: Application timestamp (parsed into a {@link Date}).</li>
     *   <li>Columns B, C, D: School name, address, and city (mapped to a {@link School}).</li>
     *   <li>Columns E-H: Contact person details (name, role, phone, and email, mapped to {@link SchoolCounselor}).</li>
     *   <li>Columns I, J: Preferred date (parsed into a {@link Date}) and time (stored as a string).</li>
     *   <li>Column K: Estimated student count (parsed into an integer).</li>
     *   <li>Column L: Event details or visitor notes (stored as a string).</li>
     * </ul>
     *
     * <p>The method validates and ensures proper mapping for all required fields.
     * If a school or counselor does not exist, it creates them using the
     * appropriate service methods. Additionally, the guide count is calculated based
     * on the estimated student count, with a maximum limit of 3 guides.</p>
     *
     * <p>Edge cases handled include:</p>
     * <ul>
     *   <li>Ensuring proper date and time parsing using {@link SimpleDateFormat}.</li>
     *   <li>Creating or updating schools and counselors dynamically based on the input.</li>
     *   <li>Limiting the maximum number of guides to 3.</li>
     * </ul>
     *
     * @param row A list of objects representing a row of data from the Google Sheet.
     *            The columns should correspond to the expected structure.
     * @return A {@link Fair} object constructed from the row data.
     * @throws ParseException If any date or time field fails to parse.
     */
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

        int guideCount = (int) Math.ceil(Integer.parseInt(row.get(10).toString()) / 50.0); // Calculate guide count
        if(guideCount > 3)  guideCount = 3; // Limit guide count to 3
        fair.setGuideCount(guideCount);

        return fair;
    }

    /**
     * Periodically fetches and saves new individual tours.
     * This method is scheduled to run every 10 seconds. It calls the {@link #saveNewIndividualTours()}
     * method to fetch data from the Google Sheet and save it into the system.
     * Any errors encountered during the process are logged to the console.
     */
    @Scheduled(fixedRate = 10000) // Runs every 10 seconds
    public void fetchAndSaveNewIndividualTours() {
        try {
            saveNewIndividualTours();
            System.out.println("Scheduled task: Individual Tours fetched and saved.");
        } catch (IOException e) {
            System.err.println("Error fetching and saving individual tours: " + e.getMessage());
        }
    }

    /**
     * Fetches individual tour data from the Google Sheet.
     * This method calls the Google Sheets API to retrieve data from the specified
     * spreadsheet and range, and returns the rows as a list of lists.
     *
     * @return A list of rows from the Google Sheet, where each row is represented
     *         as a list of objects. Returns an empty list if no data is found.
     * @throws IOException If an error occurs while communicating with the Google Sheets API.
     */
    public List<List<Object>> fetchIndividualTourData() throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(INDIVIDUAL_TOUR_SPREADSHEET_ID, INDIVIDUAL_TOUR_SPREADSHEET_RANGE)
                .execute();

        if (response.getValues() == null || response.getValues().isEmpty()) {
            System.out.println("No data found in the Google Sheet for individual tours.");
            return Collections.emptyList();
        }

        return response.getValues();
    }

    /**
     * Saves new fairs by fetching data from the Google Sheet and comparing timestamps
     * to ensure only new entries are processed and stored in the database.
     *
     * @throws IOException If an error occurs while fetching data from Google Sheets.
     */
    public void saveNewIndividualTours() throws IOException {
        // Get the latest applicationTimeStamp from the database
        Date latestTimestamp = eventService.findLatestIndividualTourApplicationTimeStamp();

        // Fetch all rows from Google Sheets
        List<List<Object>> rows = fetchIndividualTourData();

        // Prepare a list for new IndividualTour objects
        List<IndividualTour> newIndividualTours = new ArrayList<>();

        // Iterate through rows and process new applications
        for (List<Object> row : rows) {
            if (!row.isEmpty()) {
                try {
                    // Parse the timestamp
                    String timestampString = row.get(0).toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    Date rowTimestamp = dateFormat.parse(timestampString);

                    if (latestTimestamp != null && !rowTimestamp.after(latestTimestamp)) {
                        continue; // Skip old applications
                    }

                    // Map the row to an Individual Tour object
                    IndividualTour individualTour = mapRowToIndividualTour(row);

                    // Add to the list
                    newIndividualTours.add(individualTour);
                    notificationService.notifyNewIndividualTourApplication(individualTour);
                } catch (ParseException e) {
                    System.err.println("Error parsing timestamp for row: " + row);
                    e.printStackTrace();
                }
            }
        }

        // Save all new individual tours to the database
        if (!newIndividualTours.isEmpty()) {
            eventService.saveAllIndividualTours(newIndividualTours); // Assuming the same `saveAllTours()` is used for individual tours
            System.out.println("Saved " + newIndividualTours.size() + " new individual tours to the database.");
        } else {
            System.out.println("No new individual tours to save.");
        }
    }

    /**
     * Maps a row of data from the Google Sheet to an {@link IndividualTour} object.
     *
     * <p>This method parses and processes various fields in the row to construct
     * an {@link IndividualTour} object with the following mappings:
     * <ul>
     *   <li>Column A: Application timestamp (parsed into a {@link Date}).</li>
     *   <li>Columns B, C, D: Student details (name, email, phone).</li>
     *   <li>Columns E, F: School name and city (mapped to a {@link School}).</li>
     *   <li>Column G: Preferred visit date (parsed into a {@link Date}).</li>
     *   <li>Column H: Preferred visit time (stored as a string).</li>
     *   <li>Column I: Area of interest (stored as a string).</li>
     *   <li>Column J: Visitor notes or additional comments (stored as a string).</li>
     * </ul>
     *
     * <p>The method dynamically creates or updates related entities such as
     * {@link School} and {@link Student} using the appropriate service methods.</p>
     *
     * <p>Edge cases handled include:</p>
     * <ul>
     *   <li>Graceful handling of invalid date formats for timestamps and visit dates.</li>
     *   <li>Ensuring associated entities like schools and students are created or updated.</li>
     * </ul>
     *
     * @param row A list of objects representing a row of data from the Google Sheet.
     *            The columns should correspond to the expected structure.
     * @return An {@link IndividualTour} object constructed from the row data.
     * @throws ParseException If any date field fails to parse (if not handled locally).
     */
    private IndividualTour mapRowToIndividualTour(List<Object> row) throws ParseException {
        IndividualTour individualTour = new IndividualTour();

        // Parse timestamp (Column A)
        String timestampString = row.get(0).toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date applicationTimeStamp = dateFormat.parse(timestampString);
        individualTour.setApplicationTimeStamp(applicationTimeStamp);

        // Parse student details (Columns B, C, D)
        String studentName = row.get(1).toString();
        String studentEmail = row.get(2).toString();
        String studentPhone = row.get(3).toString();

        String schoolName = row.get(4).toString();
        String city = row.get(5).toString();

        School school = schoolService.findOrCreateSchool(schoolName, city, "");
        Student student = studentService.findOrCreateStudent(studentName, studentEmail, studentPhone, school);

        // Set the student object to the individual tour
        individualTour.setStudent(student);

        // Parse preferred visit date (Column G)
        String visitDateString = row.get(6).toString();
        SimpleDateFormat requestedDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date requestedVisitDate = requestedDateFormat.parse(visitDateString);
        individualTour.setDate(requestedVisitDate);

        // Parse preferred visit time (Column H)
        String hourString = row.get(7).toString();
        individualTour.setHour(hourString);

        String interestedIn = row.get(8).toString();
        individualTour.setInterestedField(interestedIn);

        // Parse visitor notes or additional comments (Column J)
        individualTour.setVisitorNotes(row.get(9).toString());

        return individualTour;
    }

}
