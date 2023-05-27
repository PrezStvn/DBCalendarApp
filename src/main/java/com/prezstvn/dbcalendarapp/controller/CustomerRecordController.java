package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.CustomerHelper;
import com.prezstvn.dbcalendarapp.model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerRecordController implements Initializable {

    public TableView customerRecordTable;
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

    private ObservableList<Customer> customerRecords;

    public void addCustomer(ActionEvent actionEvent) {
    }

    public void deleteCustomer(ActionEvent actionEvent) {
    }

    public void modifyCustomer(ActionEvent actionEvent) {
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
}
