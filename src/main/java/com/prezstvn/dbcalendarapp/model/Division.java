package com.prezstvn.dbcalendarapp.model;

import java.time.LocalDateTime;

public class Division {
    /**
     * Primary Key
     */
    private int divisionId;
    private String division;
    /**
     * Foreign Key: references Country
     */
    private int countryId;

    public Division(int id, String division, int countryId) {
        //TODO: required params ()
        this.divisionId = id;
        this.division = division;
        this.countryId = countryId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }


    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        return this.division;
    }
}
