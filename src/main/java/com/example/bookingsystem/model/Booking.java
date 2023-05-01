package com.example.bookingsystem.model;

import java.sql.Time;
import java.time.LocalDate;

public class Booking {

    private int id;

    private String firstName;

    private String lastName;

    private String organisation;

    private String email;
    
    private int phoneNumber;

    private char bookingType;

    private char catering;

    private LocalDate bookingDate;

    private LocalDate dateCreated;
    
    private String bookingCode;

    private Time startTid;

    private Time slutTid;

    private String note;

    public Booking(int id, String firstName, String lastName, String organisation, String email, int phoneNumber,
                   char bookingType,char catering, LocalDate bookingDate, LocalDate dateCreated, String bookingCode,
                   Time startTid, Time slutTid){

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bookingType = bookingType;
        this.catering = catering;
        this.bookingDate = bookingDate;
        this.dateCreated = dateCreated;
        this.bookingCode = bookingCode;
        this.startTid = startTid;
        this.slutTid = slutTid;
    }

    @Override
    public String toString() {
        /*return String.format("%-2d %-10s %-15s %-10s %-25s %-10d %-5s %-5s " +
                        "%-15s %-15s %-15s %-10s %-10s", id, firstName, lastName, organisation, email,
                phoneNumber, bookingType, catering, bookingDate, dateCreated,
                bookingCode, startTid, slutTid);*/
        return String.format("%-10s %-10s", firstName, bookingDate);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public char getBookingType() {
        return bookingType;
    }

    public void setBookingType(char bookingType) {
        this.bookingType = bookingType;
    }

    public char getCatering() {
        return catering;
    }

    public void setCatering(char catering) {
        this.catering = catering;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Time getSlutTid() {
        return slutTid;
    }


    public void setSlutTid(Time slutTid) {
        this.slutTid = slutTid;
    }

    public Time getStartTid() {
        return startTid;
    }


    public void setStartTid(Time startTid) {
        this.startTid = startTid;
    }

    public int getId() {
        return id;
    }

    public void addNote(String note){
        this.note = note;
    }
}
