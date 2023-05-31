package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.AppointmentException;
import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import com.prezstvn.dbcalendarapp.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public Button ScheduleButton;
    public Button ReportsButton;
    public Button RecordsButton;

    private int userId;



    public void ViewSchedule(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }

    public void onRecordsClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    public void onReportsClick(ActionEvent actionEvent) {
    }

    /**
     * initializing in order to run checks on logged-in users appointments
     * alert them if they have an appointment within 15 minutes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setUserId(User user) {
        this.userId = user.getUserId();
        appointmentCheck();
    }

    private void appointmentCheck() {
        try {
            ObservableList<Appointment> userAppointments = AppointmentHelper.getCustomerAppointments(userId);
            ZonedDateTime currentTime = ZonedDateTime.now();
            for(Appointment appt : userAppointments) {
                long timeDifference = ChronoUnit.MINUTES.between(currentTime, appt.getStart());
                long timeDifferenceEnd = ChronoUnit.MINUTES.between(currentTime, appt.getEnd());
                if(timeDifference <= 15 && timeDifference >= 0) throw new AppointmentException("You have an appointment that starts in " + timeDifference + " minutes located at " + appt.getLocation());
                if(timeDifference < 0 && timeDifference >= -15)  throw new AppointmentException("you have an appointment that started " + timeDifference + " minutes ago at " + appt.getLocation());
                if(timeDifference < 0 && timeDifferenceEnd > 0) throw new AppointmentException("You have an ongoing appoint at " + appt.getLocation());
            }
        } catch(SQLException e) {
            System.out.println("unable to check users schedules");
        } catch(AppointmentException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Scheduled Appointment notice!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}