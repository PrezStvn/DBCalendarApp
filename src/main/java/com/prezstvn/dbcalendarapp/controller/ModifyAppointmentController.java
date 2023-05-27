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
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Contact> contactsCombo = ContactHelper.getContacts();
            ContactComboCox.setItems(contactsCombo);
            ContactComboCox.getSelectionModel().selectFirst();
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
    
    public static void setTargetAppointment(Appointment sentAppointment) {
        targetAppointment = sentAppointment;
    }
}
