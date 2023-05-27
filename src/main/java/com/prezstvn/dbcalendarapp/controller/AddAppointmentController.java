package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.ContactHelper;
import com.prezstvn.dbcalendarapp.model.Contact;
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

public class AddAppointmentController implements Initializable {
    public TextField AppointmentId;
    public TextField AppointmentTitle;
    public TextArea AppointmentDescription;
    public TextField AppointmentLocation;
    public Button AddAppointmentSave;
    public Button CancelButton;
    public TextField AppointmentCustomerId;
    public DatePicker AppointmentStartDate;
    public DatePicker AppointmentEndDate;
    public ComboBox<Contact> ContactComboCox;
    public TextField AppointmentUserId;

    /**
     * setting up combobox values when scene is created
     * @param url
     * @param resourceBundle
     */
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
     * when the save Appt button is clicked this first executes logical checks to ensure user input fields are valid for an Appt object
     *
     * @param actionEvent SaveButton(AddAppointmentSave){horrible name} is clicked
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        //TODO: method that calls the save here
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    /**
     * Scene is switched back to Schedule and no other actions are taken
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }


}
