package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {
    public RadioButton weekRadio;
    public ToggleGroup scheduleGroup;
    public RadioButton monthRadio;
    public TableView<Appointment> scheduleTable;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, Integer> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, LocalDateTime> startDateColumn;
    public TableColumn<Appointment, LocalDateTime> endDateColumn;
    public TableColumn<Appointment, Integer> customerIdColumn;
    public TableColumn<Appointment, Integer> userIdColumn;
    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public Button MainMenu;

    private ObservableList<Appointment> appointmentSchedule;

    /**
     * gets values from the DB and then sets them in TableView scheduleTable
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentSchedule = AppointmentHelper.getAllAppointments();
            scheduleTable.setItems(appointmentSchedule);
            setSchedule();
        } catch (SQLException e) {
            System.out.println("something went wrong retrieving appointments from the DB.");
            throw new RuntimeException(e);
        }

    }

    private void setSchedule() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));;
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));;
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));;
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));;
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));;
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));;
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));;
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));;
        }

    public void onWeekSelect(ActionEvent actionEvent) {
    }

    public void onMonthSelect(ActionEvent actionEvent) {
    }

    public void addAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/AddAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void deleteAppointment(ActionEvent actionEvent) {
    }

    public void modifyAppointment(ActionEvent actionEvent) throws IOException {
        try {
            Appointment targetAppointment = scheduleTable.getSelectionModel().getSelectedItem();
            if (targetAppointment == null) {
                throw new Exception("Please select an Appointment to modify.");
            }
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/ModifyAppointment.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch(Exception e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void onMainMenuClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
}
