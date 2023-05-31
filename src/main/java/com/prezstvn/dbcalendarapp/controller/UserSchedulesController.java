package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.helper.LoginHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import com.prezstvn.dbcalendarapp.model.User;
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

/**
 * I do not know if this is bad practice to be able to view all users appointments
 * but this is my custom report
 * giving the users a way to view all other users schedules
 */
public class UserSchedulesController implements Initializable {
    public ComboBox<User> UserComboBox;
    public TableView<Appointment> UserSchedule;
    public TableColumn appointmentId;
    public TableColumn appointmentTitle;
    public TableColumn appointmentType;
    public TableColumn appointmentDescription;
    public TableColumn appointmentStart;
    public TableColumn appointmentEnd;
    public TableColumn customerId;
    public Button MainMenuButton;

    /**
     * queries the database for all appointments with userId
     * updates current TableView once list of appointments is retrieved
     * @param actionEvent a selection in UserComboBox is made
     */
    public void userSelected(ActionEvent actionEvent) {
        try {
            int userId = UserComboBox.getSelectionModel().getSelectedItem().getUserId();
            ObservableList<Appointment> usersAppointments = AppointmentHelper.getUserAppointments(userId);
            UserSchedule.setItems(usersAppointments);
        } catch(SQLException e ) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contact Schedule error");
            alert.setContentText("An internal error occurred when trying to retrieve contact appointments from the DB");
            alert.showAndWait();
        }

    }

    /**
     * switch back to the main menu
     * @param actionEvent
     * @throws IOException
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 200));
        stage.show();
    }

    /**
     * on scene creation grab list of users and populate UserComboBox with these values
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableColumns();
        try {
            ObservableList userList = LoginHelper.getListOfUsers();
            UserComboBox.setItems(userList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * map appointment fields to table columns
     */
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
