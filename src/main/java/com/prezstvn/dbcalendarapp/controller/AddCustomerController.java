package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.AppointmentException;
import com.prezstvn.dbcalendarapp.exception.CustomerCreationException;
import com.prezstvn.dbcalendarapp.helper.CustomerHelper;
import com.prezstvn.dbcalendarapp.model.Country;
import com.prezstvn.dbcalendarapp.model.Customer;
import com.prezstvn.dbcalendarapp.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

    public TextField CustomerId;
    public TextField CustomerName;
    public TextField CustomerAddress;
    public TextField PostalCode;
    public Button AddCustomerSave;
    public Button CancelButton;
    public ComboBox<Country> CountryComboBox;
    public TextField Phone;
    public ComboBox<Division> DivisionComboBox;
    private ObservableList<Division> divisionObservableList;

    /**
     * when scene is created: CountrycomboBox is populated with the resuled of the query to CustomerHelper
     * then we get the full list of Divisions to be filtered when a country selection is made
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set values for CountryComboBox
        try {
            CountryComboBox.setItems(CustomerHelper.getCountryList());
            divisionObservableList = CustomerHelper.getDivisionList();
        } catch(SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * retrieves selected country's ID and adds one to get the bases to match
     * then passes this to setDivisionComboBox
     * @param actionEvent a country is selected
     * @throws SQLException
     */
    public void onCountrySelection(ActionEvent actionEvent) throws SQLException {
        setDivisionComboBox(CountryComboBox.getSelectionModel().getSelectedIndex() + 1);
    }

    /**
     * rewrote this method(used to be identical to the setDivisionComboBox in modifyCustomerController
     * since the set of data retrieved from the DB is finite, it is much more resource intensive to repeatedly query
     * the DB everytime a new countrySelection is made.
     * stored local list of firstLevelDivisions here and sorted through them based on countryId
     * @param countryId country to filter divisions list by
     * @throws SQLException
     */
    private void setDivisionComboBox(int countryId) throws SQLException {
        DivisionComboBox.setItems(filterDivisionByCountry(countryId));
    }

    /**
     * filtering the locally saved list of divisions based on currently selected country's ID
     * @param countryId
     * @return
     */
    private ObservableList<Division> filterDivisionByCountry(int countryId) {
        ObservableList<Division> filteredDivisions = FXCollections.observableArrayList();
        for(Division div : divisionObservableList) {
            if (div.getCountryId() == countryId) {
                filteredDivisions.add(div);
            }
        }
        return filteredDivisions;
    }

    /**
     *
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            Customer createdCustomer = isValidCustomer();
            CustomerHelper.createCustomer(createdCustomer);
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Records");
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch(CustomerCreationException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Customer Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch(SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Combo Box Error");
            alert.setContentText("Please select a Country and Division");
            alert.showAndWait();
        }
    }
    //TODO: Finish checks here

    /**
     * void if any of the checks fails it will throw and exception and exit
     * exception will be caught and handled in parent method onSaveClick()
     * if Either combo box has no selection the try block will throw a NullPointerException
     * this will be caught and then thrown as a CustomerCreationException as well.
     * @throws CustomerCreationException if any of the fields are invalid values this will be thrown
     * and then displayed to the user
     */
    private Customer isValidCustomer() throws CustomerCreationException {

        String name = CustomerName.getText();
        String address = CustomerAddress.getText();
        String postalCode = PostalCode.getText();
        String phone = Phone.getText();
        int divisionId;
        try {
            divisionId = DivisionComboBox.getSelectionModel().getSelectedItem().getDivisionId();
            //next line is most likely unnecessary since DivisionComboBox will have no items unless a selection is made in the CountryComboBox
            int countryId = CountryComboBox.getSelectionModel().getSelectedItem().getCountryId();
        } catch(NullPointerException e) {
            throw new CustomerCreationException("Please select a Country and Division from their comboboxes");
        }
        if(name.equals("")) throw new CustomerCreationException("Please enter a name.");
        if(address.equals("")) throw new CustomerCreationException("Please enter a valid address");
        if(postalCode.equals("")) throw new CustomerCreationException("Please enter a Postal Code");
        if(phone.equals("")) throw new CustomerCreationException("Please enter Customer Phone number before saving");
        return new Customer(name, address, postalCode, phone, divisionId);
    }

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Records");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }


}
