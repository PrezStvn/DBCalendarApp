package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.CustomerHelper;
import com.prezstvn.dbcalendarapp.model.Customer;
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

public class CustomerRecordController implements Initializable {

    public TableView<Customer> customerRecordTable;
    public TableColumn customerIdColumn;
    public TableColumn nameColumn;
    public TableColumn addressColumn;
    public TableColumn postalCodeColumn;
    public TableColumn phoneColumn;
    public TableColumn divisionIdColumn;
    public TableColumn countryIdColumn;
    public Button addCustomerButton;
    public Button deleteCustomerButton;
    public Button modifyCustomerButton;
    /**
     * todo: populate combo boxes with string int pairs.
     * add an on action that dynamically generates the values in second combo box based on country
     */
    public ComboBox CountryCombo;
    /**
     * todo: on action that filters customer records based on selected division
     */
    public ComboBox DivisionCombo;
    public Button MainMenu;

    private ObservableList<Customer> customerRecords;

    public void addCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/AddCustomerRecord.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Customer Form");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    public void deleteCustomer(ActionEvent actionEvent) {
    }

    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
        try {
            Customer targetCustomer = customerRecordTable.getSelectionModel().getSelectedItem();
            if(targetCustomer == null) {
                throw new Exception("Please select a Customer Record to modify");
            }
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/ModifyCustomer.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Customer Form");
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Customer Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerRecords = CustomerHelper.getCustomerRecords();
            customerRecordTable.setItems(customerRecords);
            setCustomerRecordTableColumns();
        } catch (SQLException e) {
            System.out.println("failed to load customer records");
            throw new RuntimeException(e);
        }
    }

    private void setCustomerRecordTableColumns() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));;
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));;
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));;
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));;
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));;
        countryIdColumn.setCellValueFactory(new PropertyValueFactory<>("countryId"));;
    }

    public void onMainMenuClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
}
