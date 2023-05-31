package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.helper.ContactHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import com.prezstvn.dbcalendarapp.model.Contact;
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
import java.util.ResourceBundle;

public class ContactScheduleController implements Initializable {
    public ComboBox<Contact> ContactComboBox;
    public TableColumn appointmentId;
    public TableColumn appointmentTitle;
    public TableColumn appointmentType;
    public TableColumn appointmentDescription;
    public TableColumn appointmentStart;
    public TableColumn appointmentEnd;
    public TableColumn customerId;
    public Button MainMenuButton;
    public TableView ContactSchedule;


    public void contactSelected(ActionEvent actionEvent) {
        try {
            int contactId = ContactComboBox.getSelectionModel().getSelectedItem().getContactId();
            ObservableList<Appointment> contactsAppointments = AppointmentHelper.getContactAppointments(contactId);
            ContactSchedule.setItems(contactsAppointments);
        } catch(SQLException e ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contact Schedule error");
            alert.setContentText("An internal error occurred when trying to retrieve contact appointments from the DB");
            alert.showAndWait();
        }
        }

    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    /**
     * getting current list of contacts and setting ContactComboBox values with the retrieved list
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setTableColumns();
            ObservableList<Contact> contactsCombo = ContactHelper.getContacts();
            ContactComboBox.setItems(contactsCombo);
        } catch (SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contact information retrieval Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setTableColumns() {
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }
}
