package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.ContactHelper;
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
    public ComboBox<String> ContactComboCox;
    public TextField AppointmentUserId;
    public TextArea AppointmentDescription;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> contactsCombo = ContactHelper.getContacts();
            ContactComboCox.setItems(contactsCombo);
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    public void onSaveClick(ActionEvent actionEvent) {
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }
}
