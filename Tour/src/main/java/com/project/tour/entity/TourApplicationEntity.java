package com.project.tour.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "tour_applications")
public class TourApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(nullable = false)
    private String city;

    @Column(name = "desired_visit_date", nullable = false)
    private java.time.LocalDate desiredVisitDate;

    @Column(name = "visit_time", nullable = false)
    private java.time.LocalTime visitTime;

    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;

    @Column(name = "group_leader_name", nullable = false)
    private String groupLeaderName;

    @Column(name = "group_leader_role")
    private String groupLeaderRole;

    @Column(name = "group_leader_phone", nullable = false)
    private String groupLeaderPhone;

    @Column(name = "group_leader_email", nullable = false)
    private String groupLeaderEmail;


    @Column(name = "visitor_notes")
    private String visitorNotes;
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public java.time.LocalDate getDesiredVisitDate() {
        return desiredVisitDate;
    }

    public void setDesiredVisitDate(java.time.LocalDate desiredVisitDate) {
        this.desiredVisitDate = desiredVisitDate;
    }

    public java.time.LocalTime getVisitTime() {
        return visitTime;
    }
    public void setVisitTime(java.time.LocalTime visitTime) {
        this.visitTime = visitTime;
    }
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName;
    }

    public String getGroupLeaderRole() {
        return groupLeaderRole;
    }

    public void setGroupLeaderRole(String groupLeaderRole) {
        this.groupLeaderRole = groupLeaderRole;
    }

    public String getGroupLeaderPhone() {
        return groupLeaderPhone;
    }

    public void setGroupLeaderPhone(String groupLeaderPhone) {
        this.groupLeaderPhone = groupLeaderPhone;
    }

    public String getGroupLeaderEmail() {
        return groupLeaderEmail;
    }

    public void setGroupLeaderEmail(String groupLeaderEmail) {
        this.groupLeaderEmail = groupLeaderEmail;
    }

    public String getVisitorNotes() {
        return visitorNotes;
    }
    public void setVisitorNotes(String visitorNotes) {
        this.visitorNotes = visitorNotes;
    }
}