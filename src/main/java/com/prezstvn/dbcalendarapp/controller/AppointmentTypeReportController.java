package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentTypeReportController implements Initializable {

    public ScrollPane reportPane;
    public TextArea reportTextArea;
    public Button toMainMenu;

    private ObservableList<Appointment> allAppointments;

    private int currYear;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allAppointments = AppointmentHelper.getAllAppointments();
            currYear = ZonedDateTime.now().getYear();
            writeReport();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * iterates through the java.time.Month object to pull appointments for each month
     * then we enter a second loop inside that connected to a map and using i to iterate over that map
     * to filter appointments of type[i] for each month and append that information to our stringBuilder
     */
    private void writeReport() {
        int month = 0;
        Map<Integer, String> typeMap = new HashMap<Integer, String>();
        typeMap.put(0, "Planning Session");
        typeMap.put(1, "De-Briefing");
        typeMap.put(2, "Marketing");
        typeMap.put(3, "Coffee");
        StringBuilder thisMonthsReport = new StringBuilder();
        for(Month m : Month.values()) {

           thisMonthsReport.append(m.toString()).append(":\n");
           //LAMBDA: first filtering down to narrow scope and only view appointments for the current month(current in the for each loop)
            ObservableList<Appointment> thisMonthsAppointments = allAppointments.stream().filter(appt ->
                                        appt.getStart().getMonthValue() == m.getValue()).collect(Collectors.toCollection(FXCollections::observableArrayList));
                for(int i = 0; i < typeMap.size(); i++) {
                    int finalI = i;
                    //LAMBDA: used on the creation o the oList to filter out anything from the current month that is not of type(i)
                    ObservableList<Appointment> numberOfAppointmentsOfType = thisMonthsAppointments.stream()
                            .filter(appt -> appt.getType().equals(typeMap.get(finalI))).collect(Collectors.toCollection(FXCollections::observableArrayList));
                    thisMonthsReport.append(typeMap.get(i)).append(": ").append(numberOfAppointmentsOfType.size()).append("\n");
                }
        }
        reportTextArea.appendText(thisMonthsReport.toString());
    }

    public void mainMenuClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
}
