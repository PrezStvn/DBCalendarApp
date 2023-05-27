module com.prezstvn.dbcalendarapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.prezstvn.dbcalendarapp to javafx.fxml;
    exports com.prezstvn.dbcalendarapp;
    exports com.prezstvn.dbcalendarapp.controller;
    exports com.prezstvn.dbcalendarapp.model;
    opens com.prezstvn.dbcalendarapp.controller to javafx.fxml;
}