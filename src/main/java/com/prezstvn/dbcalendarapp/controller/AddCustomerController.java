package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.CustomerCreationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCustomerController {

    public TextField CustomerId;
    public TextField CustomerName;
    public TextField CustomerAddress;
    public TextField PostalCode;
    public Button AddCustomerSave;
    public Button CancelButton;
    public ComboBox CountryComboBox;
    public TextField Phone;
    public ComboBox DivisionComboBox;

    public void onCountrySelection(ActionEvent actionEvent) {
    }

    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            isValidCustomer();
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Records");
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch(CustomerCreationException e) {

        }
    }

    private void isValidCustomer() throws CustomerCreationException {
        throw new CustomerCreationException("fuckoff");
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Records");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }
}
