package com.prezstvn.dbcalendarapp.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Appointment {
    //Primary key
    private int appointmentId;
    // title ->  entry for Title on Appointment mySQL table
    private String title;
    // description of appointment
    private String description;
    // location of appointment
    private String location;
    // type of appointment
    private String type;
    // start date and time of the appointment
    private ZonedDateTime start;
    // end date and time of appointment
    private ZonedDateTime end;
    // Foreign key references Customer
    private int customerId;
    // Foreign key
    private int userId;
    // Foreign key
    private int contactId;
    private String contactName;
    public Appointment(){}
    public Appointment(int appointmentId,
                       String title,
                       String description,
                       String location,
                       String type,
                       ZonedDateTime start,
                       ZonedDateTime end,
                       int customerId,
                       int userId,
                       int contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }
    //getter for appointmentId
    public int getAppointmentId() {
        return appointmentId;
    }
    //setter for appointmentId
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    //getter for title
    public String getTitle() {
        return title;
    }
    // setter for title
    public void setTitle(String title) {
        this.title = title;
    }
    //getter for description
    public String getDescription() {
        return description;
    }
    //setter for description
    public void setDescription(String description) {
        this.description = description;
    }
    //getter for location
    public String getLocation() {
        return location;
    }
    //setter for lcoation
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * getter for type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * setter for appointment type
     * @param type type to set this.type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter for start date and time
     * @return the start date and time of the appt
     */
    public ZonedDateTime getStart() {
        return start;
    }

    /**
     * setter for appointment start date and time
     * @param start the zoneddatetime object to set the start time with
     */
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    /**
     * getter for end of appointment
     * @return the scheduled end date and time for the appt
     */
    public ZonedDateTime getEnd() {
        return end;
    }

    /**
     * setter for appointment end date and time
     * @param end the ZonedDateTime to set end to
     */
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    /**
     * getter for customerId
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * setter for customerId
     * @param customerId the value to set customerId to
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * getter for userId
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * setter for userId
     * @param userId value to set local userId to
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * getter contactId
     * @return contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * setter contactId
     * @param contactId value to this.contactId to
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
