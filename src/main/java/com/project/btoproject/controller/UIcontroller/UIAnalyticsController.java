package com.project.btoproject.controller.UIcontroller;

import com.project.btoproject.dto.SchoolTourCountDTO;
import com.project.btoproject.enums.SchoolType;
import com.project.btoproject.enums.Status;
import com.project.btoproject.enums.Tier;
import com.project.btoproject.model.*;
import com.project.btoproject.repository.HighSchoolRepository;
import com.project.btoproject.repository.SchoolRepository;
import com.project.btoproject.service.AllUsersService;
import com.project.btoproject.service.EventService;
import com.project.btoproject.service.SchoolService;
import com.project.btoproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/schools")
public class UIAnalyticsController {

    @Autowired
    private final EventService eventService;
    private final HighSchoolRepository highSchoolRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolService schoolService;
    private final AllUsersService allUsersService;

    public UIAnalyticsController(HighSchoolRepository highSchoolRepository, SchoolService schoolService, EventService eventService, SchoolRepository schoolRepository, AllUsersService allUsersService) {
        this.highSchoolRepository = highSchoolRepository;
        this.schoolService = schoolService;
        this.eventService = eventService;
        this.schoolRepository = schoolRepository;
        this.allUsersService = allUsersService;
    }

    @GetMapping("/tour-counts")
    public String getSchoolTourCounts(@RequestParam(required = false) String schoolName, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String role="";
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse("ROLE_UNKNOWN");
        }
        model.addAttribute("role", role);


        User user = allUsersService.getUserById(Long.parseLong(username)).get();
        List<UserTask> tasks = allUsersService.seeAllTasks(user);
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);



        List<School> schools = schoolRepository.findAll();
        List<HighSchoolForStatistics> highSchools = highSchoolRepository.findAll();

        // Calculate the total student count across all high schools
        long totalStudentCount = highSchools.stream()
                .mapToLong(HighSchoolForStatistics::getStudentCount)
                .sum();

        // Create a map to store the total student count for each school
        Map<String, Long> schoolStudentCounts = highSchools.stream()
                .collect(Collectors.groupingBy(HighSchoolForStatistics::getName,
                        Collectors.summingLong(HighSchoolForStatistics::getStudentCount)));

        model.addAttribute("schools", schools);
        model.addAttribute("schoolStudentCounts", schoolStudentCounts);
        model.addAttribute("totalStudentCount", totalStudentCount);

        // Fetch the tour counts
        List<Tour> allTours = eventService.getAllTours();
        List<Tour> completedTours = allTours.stream()
                .filter(tour -> tour.getStatus() == Status.COMPLETED_TOUR)
                .collect(Collectors.toList());
        if(completedTours.size() < 4) {
            return "Analytics-2.0";
        }
        Map<School, Long> tourCounts = completedTours.stream()
                .filter(tour -> schoolName == null || tour.getSchool().getName().equals(schoolName))
                .collect(Collectors.groupingBy(Tour::getSchool, Collectors.counting()));
        // Prepare data for the view
        List<SchoolTourCountDTO> tourCountDTOs = tourCounts.entrySet().stream()
                .map(entry -> {
                    School school = entry.getKey();
                    Tier tier = school.getTier() != null ? school.getTier() : Tier.THIRD_TIER;
                    float percentage = 0.0f;

                    // Use case-insensitive key comparison
                    String lowerCaseSchoolName = school.getName().toLowerCase(); // Convert the name to lowercase

                    if (schoolStudentCounts.keySet().stream().anyMatch(name -> name.equalsIgnoreCase(lowerCaseSchoolName)) && totalStudentCount > 0) {
                        percentage = (schoolStudentCounts.entrySet().stream()
                                .filter(e -> e.getKey().equalsIgnoreCase(lowerCaseSchoolName))
                                .mapToLong(Map.Entry::getValue)
                                .findFirst()
                                .orElse(0L) / (float) totalStudentCount) * 100;
                    }

                    // Format percentage to 2 decimal places
                    String formattedPercentage = String.format("%.2f", percentage);

                    return new SchoolTourCountDTO(school.getName(), entry.getValue(), tier, school.getId(), Float.parseFloat(formattedPercentage));
                })
                .sorted((a, b) -> b.getTourCount().compareTo(a.getTourCount())) // Sort by tour count descending
                .collect(Collectors.toList());


        // Extract top 4 schools
        List<SchoolTourCountDTO> topSchools = tourCountDTOs.stream().limit(4).collect(Collectors.toList());
        model.addAttribute("tourCounts", tourCounts);
        model.addAttribute("tourCountDTOs", tourCountDTOs); // Full list for table
        model.addAttribute("topSchools", topSchools);   // Top 4 for the chart
        model.addAttribute("schoolName", schoolName);

        Set<School> allSchools = allTours.stream()
                .filter(tour -> schoolName == null || tour.getSchool().getName().equals(schoolName))
                .map(Tour::getSchool) // Extract the School from the Tour
                .collect(Collectors.toSet());
        // Count the number of Public and Private schools
        long publicSchoolCount = allSchools.stream()
                .filter(school -> SchoolType.STATE == school.getSchoolType())
                .count();
        long privateSchoolCount = allSchools.stream()
                .filter(school -> SchoolType.PRIVATE == school.getSchoolType())
                .count();
        long totalSchools = allSchools.size();
        double privatePercentage = (double) privateSchoolCount / totalSchools * 100;
        double publicPercentage = (double) publicSchoolCount / totalSchools * 100;
        String privateColor = "#303568"; // Example: Custom color for private schools
        String publicColor = "#c00d0a"; // Example: Custom color for public schools
        model.addAttribute("privatePercentage", privatePercentage);
        model.addAttribute("publicPercentage", publicPercentage);
        model.addAttribute("privateColor", privateColor);
        model.addAttribute("publicColor", publicColor);

        Map<String, Long> cityCounts = allSchools.stream()
                .collect(Collectors.groupingBy(School::getCity, Collectors.counting()));

        Map<String, Long> topCities = cityCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) // Sort by count descending
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Use the key from the entry
                        Map.Entry::getValue, // Use the value from the entry
                        (oldValue, newValue) -> oldValue, // Merge function (not needed here as there are no duplicates)
                        LinkedHashMap::new // Maintain insertion order for sorted elements
                ));
        long totalCities = cityCounts.values().stream().mapToLong(Long::longValue).sum();
        Map<String, Double> cityPercentages = topCities.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            double percentage = (entry.getValue() * 100.0) / totalCities;
                            // Format the percentage to 1 decimal place
                            return Double.valueOf(String.format("%.1f", percentage));
                        }// Calculate percentage
                ));
        List<String> colors = Arrays.asList(
                "#AC0D0D", // Deep crimson red
                "#30356E", // Rich dark blue
                "#960000", // Bold ruby red
                "#4A4A50", // Neutral dark gray
                "#6C6F94", // Cool slate blue
                "#C80706", // Vivid scarlet red
                "#2E3A59", // Steely blue-gray
                "#A60021", // Strong burgundy red
                "#2C2F33", // Charcoal gray
                "#5C6D92"  // Muted navy blue
        );



        Map<String, String> cityColors = new HashMap<>();
        int colorIndex = 0;
        for (String city : cityPercentages.keySet()) {
            cityColors.put(city, colors.get(colorIndex % colors.size())); // Use modulo to cycle through colors
            colorIndex++;
        }
// Prepare the conic-gradient string
        StringBuilder gradientBuilder = new StringBuilder();
        double cumulativePercentage = 0.0;
        for (Map.Entry<String, Double> entry : cityPercentages.entrySet()) {
            String color = cityColors.get(entry.getKey());
            double start = cumulativePercentage;
            cumulativePercentage += entry.getValue();
            gradientBuilder.append(color).append(" ")
                    .append(start).append("% ")
                    .append(cumulativePercentage).append("%, ");
        }
// Remove the trailing comma and space
        String gradientString = gradientBuilder.substring(0, gradientBuilder.length() - 2);

        model.addAttribute("cityGradient", gradientString);
        model.addAttribute("cityColors", cityColors);
        double maxTourCount = topSchools.get(0).getTourCount();
        int interval = (int) (maxTourCount / 10); // Adjust interval as needed
        model.addAttribute("maxTourCount", (int) maxTourCount); // Pass as integer for cleaner output
        model.addAttribute("interval", interval);
        System.out.println("REREREREREREGGGHEGEGEG");
        // Prepare data for the view
        model.addAttribute("cityPercentages", cityPercentages);
        model.addAttribute("cityCounts", cityCounts);
        model.addAttribute("totalCities", totalCities);
        model.addAttribute("topCities", topCities);

        return "Analytics"; // Name of the Thymeleaf template
    }

    @PostMapping("/setTierStatus")
    public String saveTiers(@RequestParam Long id, @RequestParam Tier tier, Model model) {
        School school = schoolRepository.findById(id).get();
        schoolService.setSchoolTier(school,tier);
        return "redirect:/ui/schools/tour-counts"; // Redirect to a page after the update
    }
    @PostMapping("/seeSchoolTours")
    public String schoolTours(@RequestParam(required = false) String schoolName, @RequestParam Long id, Model model) {
        School school = schoolRepository.findById(id).get();
        List<Tour> allTours = eventService.getAllTours();
        Map<School, List<Tour>> tours = allTours.stream()
                .filter(tour -> schoolName == null || tour.getSchool().getName().equals(schoolName))
                .collect(Collectors.groupingBy(Tour::getSchool));
        return "redirect:/ui/schools/tour-counts"; // Redirect to a page after the update
    }

}
