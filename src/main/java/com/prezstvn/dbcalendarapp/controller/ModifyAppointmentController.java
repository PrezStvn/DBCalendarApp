package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.AppointmentException;
import com.prezstvn.dbcalendarapp.helper.ContactHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import com.prezstvn.dbcalendarapp.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

/**
 * FUTURE ENHANCEMENT: it may be more intelligent to combine both the controllers and the views for
 * add and modify CustomerRecords(also for Appointments) the functionality and style of both pages are near identical
 * both modify and create take in all the same info just a logical choice would need to be made at page creation
 * to set page as add Customer or Modify Customer
 *
 */

public class ModifyAppointmentController implements Initializable {
    public TextField AppointmentId;
    public TextField AppointmentTitle;
    public TextField AppointmentLocation;
    public TextField AppointmentCustomerId;
    public Button ModifyAppointmentSave;
    public Button CancelButton;
    public DatePicker AppointmentStartDate;
    public DatePicker AppointmentEndDate;
    public ComboBox<Contact> ContactComboCox;
    public TextField AppointmentUserId;
    public TextArea AppointmentDescription;
    public ComboBox<String> AppointmentTypeComboBox;

    private Appointment targetAppointment;
    public ComboBox<LocalTime> StartTimeComboBox;
    public ComboBox<LocalTime> EndTimeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Contact> contactsCombo = ContactHelper.getContacts();
            ContactComboCox.setItems(contactsCombo);
            ContactComboCox.getSelectionModel().selectFirst();
            setTimes();
            AppointmentStartDate.setValue(LocalDate.now());
            AppointmentEndDate.setValue(LocalDate.now());
            StartTimeComboBox.getSelectionModel().select(LocalTime.of(8, 0));
            EndTimeComboBox.getSelectionModel().select(LocalTime.of(8, 30));
        } catch(SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * attempt to save Appointment with saveAppointmentLogicCheck()
     * if successful we do some housekeeping and reset static targetAppointment to null
     * may not be necessary but seems advisable
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            Appointment appointmentToAdd = isValidAppointment();
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Schedule");
            stage.setScene(new Scene(root, 900, 400));
            stage.show();
        } catch(AppointmentException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setTimes() {
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 44);

        // Populate the ComboBox with time values in 15-minute increments
        while (startTime.isBefore(endTime) || startTime.equals(endTime)) {
            StartTimeComboBox.getItems().add(startTime);
            EndTimeComboBox.getItems().add(startTime);
            startTime = startTime.plusMinutes(15);
        }
    }

    private Appointment isValidAppointment() throws AppointmentException {
        Appointment appointmentToAdd =  new Appointment();
        //TODO: Logic checks mainly time constraints est 0800-2200
        String title = AppointmentTitle.getText();
        String description = AppointmentDescription.getText();
        String location = AppointmentLocation.getText();
        String type;
        ZonedDateTime startTime = LocalDateTime.of(AppointmentStartDate.getValue(), StartTimeComboBox.getValue()).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.of(AppointmentEndDate.getValue(), EndTimeComboBox.getValue()).atZone(ZoneId.systemDefault());
        int customerId;
        int userId;
        int contactId = ContactComboCox.getSelectionModel().getSelectedItem().getContactId();
        try {
            customerId = Integer.parseInt(AppointmentCustomerId.getText());
            userId = Integer.parseInt(AppointmentUserId.getText());
        } catch(Exception e) {
            throw new AppointmentException("Customer_ID and User_ID most both be positive integers and not blank");
        }

        return appointmentToAdd;
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }
    
    public void setTargetAppointment(Appointment sentAppointment) {
        targetAppointment = sentAppointment;
        setFormFields();
    }

    /**
     * setting all forms initial values based on those in targetAppointment
     * TODO: add field for the current time as well maybe another combo with 15 min intervals;
     */
    private void setFormFields() {
        AppointmentId.setText(String.valueOf(targetAppointment.getAppointmentId()));
        AppointmentTitle.setText(targetAppointment.getTitle());
        AppointmentDescription.setText(targetAppointment.getDescription());
        AppointmentLocation.setText(targetAppointment.getLocation());
        AppointmentStartDate.setValue(targetAppointment.getStart().toLocalDate());
        AppointmentEndDate.setValue(targetAppointment.getEnd().toLocalDate());
        AppointmentCustomerId.setText(String.valueOf(targetAppointment.getCustomerId()));
        setContactComboBox();
        setAppointmentTypes();
        AppointmentUserId.setText(String.valueOf(targetAppointment.getUserId()));
    }

    private void setAppointmentTypes() {
        ObservableList<String> typesList = FXCollections.observableArrayList();
        typesList.add("Planning Session");
        typesList.add("De-Briefing");
        typesList.add("Marketing");
        typesList.add("Coffee");
        AppointmentTypeComboBox.setItems(typesList);
    }

    /**
     * iterating through contacts in the contactcomboBox
     * once matching contact is found the index is returned then
     * that index is used to set the value displayed in the ContactComboBox
     */
    private void setContactComboBox() {
        ObservableList<Contact> contactList = ContactComboCox.getItems();
        int indexOfContactInList = 0;
        for(Contact contact : contactList) {
            if(contact.getContactId() == targetAppointment.getContactId()) break;
            else indexOfContactInList++;
        }
        ContactComboCox.getSelectionModel().select(indexOfContactInList);
    }
}
