package com.prezstvn.dbcalendarapp.controller;

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

public class ModifyCustomerController {
    public TextField CustomerId;
    public TextField CustomerName;
    public TextField CustomerAddress;
    public TextField PostalCode;
    public TextField Phone;
    public ComboBox CountryComboBox;
    public ComboBox DivisionComboBox;
    public Button ModifyCustomerSave;
    public Button CancelButton;

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
        } catch(Exception e) {
            
        }
    }

    private void isValidCustomer() {
        //TODO: all logical checks and error messages written for all input values.

    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Records");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }
}
