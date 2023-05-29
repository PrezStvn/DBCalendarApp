package com.prezstvn.dbcalendarapp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Country {
    /**
     * Primary Key
     */
    private int countryId;
    private String country;
    //private ObservableList<Division> firstLevelDivisions;

    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
        //this.firstLevelDivisions = FXCollections.observableArrayList();
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return this.country;
    }
}
