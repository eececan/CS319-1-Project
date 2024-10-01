
# CS319-1-Project
## University Tour Management System
This project is a comprehensive application designed to streamline the process of prospective students visiting our university. It enables users to schedule and attend campus tours, while also providing management tools for guides, coordinators etc. The project also allows schools to invite Bilkent to their fairs.

The platform offers various features for different user roles, including upcoming event notifications, tour registrations, guide rankings, and tour-related data management.

## Table of Contents
1. Tech Stack
2. Features
3. User Roles
4. Setup
5. Usage
6. Contributing
7. License

### 1. Tech Stack
This project is built using the following technologies:
- Spring Boot
- Java
- MVC Architecture
- PostgreSQL
- Redis
- JavaScript
- Features

### 2. General Features
- Multi-language support: The system allows users to access the platform in multiple languages (Translate feature). 
- Event Management: Users can view and manage upcoming events. 
- Notifications: Timely notifications for users regarding events and tours. 
- QR Code Registration: Register for tours using QR codes. 
- Prioritization of High Schools (HS): Specific priorities can be assigned for HS tours. 
- Guide Ranking: Users can rate and rank guides based on their experience. 
- Terms and Conditions: Include a section to accept legal terms and conditions. 
- FAQ and Q&A Section: Help users with frequently asked questions. 
- Tour Tracking: View tours in progress, upcoming tours, and completed tours. 
- Tour Data: Manage data on tour guides, group sizes, and more. 
- Admin & Coordinator Features:
- Dashboard: A customized dashboard for each type of user (coordinator, guide, admin). 
- Guide Attendance: Track attendance for guides assigned to specific tours. 
- School Selection: Schools can be selected and linked to specific tours. 
- Individual Tours: Support for one-on-one tours between a student and a guide.  
- Feedback Forms: Collect feedback from students about their tour experience. 

### 3. User Roles 
There are several user roles within the system, each with different permissions:
1. Head of the Office (Örsan Örge):
	- Coordinates the university fairs.
	- Has a dashboard for high school statistics.
	- Views the performance of the guides.
2. Secretary (Dilek Yıldız):
	- Sends the acceptance e-mails to the high schools (school counselors) **after advisors accept a tour application**.
3. Coordinator: 
	- Coordinates the university fairs and notifies guides about the university fairs.
	- Views all the guides and their performance.
	- Has a dashboard for high school statistics.
	- Views all the events.
4. Advisor: 
	- Advisors manage the applications made by school counselors or individual students. 
	- **Maybe** manually selects guides for accepted tours. 
	- Each advisor is responsible for managing all tours **scheduled for a specific day**.
	- Advisors must consider the priorities while accepting high school applications.
5. Guide: 
	- Chooses an accepted tour for guidance if they are available.
	- Logs their work hours for tours, individual tours, or university fairs (For monthly payment). Note that this log system might be unnecessary as the system might also record the guides' working hours. 
	- Leads tours for prospective students and provides information about the university. 
	- **Note:** Prospective guides in training might be added as another user.
6. Prospective Student:
	- Applies for individual visits.
	- Has to enter **additional details**. For example his/her major of interest.
7. School Counselor: 
	- Represents their high school for school tours.
	- Applies to a specific period for a visit.
	- Also invites Tanıtım Ofisi to university fairs in the high school.
8. Admin: 
	- The need for an admin user will be discussed and implemented if necessary.
