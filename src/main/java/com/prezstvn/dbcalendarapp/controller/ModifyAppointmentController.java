package com.prezstvn.dbcalendarapp.controller;

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
import java.time.LocalDate;
import java.time.LocalTime;
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
    
    private static Appointment targetAppointment;
    public ComboBox<LocalTime> StartTimeComboBox;
    public ComboBox<LocalTime> EndTimeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Contact> contactsCombo = ContactHelper.getContacts();
            ContactComboCox.setItems(contactsCombo);
            ContactComboCox.getSelectionModel().selectFirst();
            setTimes();
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
        //TODO: SET static appointment back to null.
        if(isValidAppointment()) {
            targetAppointment = null;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
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

    private boolean isValidAppointment() {
        boolean isValid = false;

        return isValid;
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
        AppointmentUserId.setText(String.valueOf(targetAppointment.getUserId()));
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
