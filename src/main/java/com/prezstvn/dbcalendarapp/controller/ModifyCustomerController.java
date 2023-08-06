package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.CustomerCreationException;
import com.prezstvn.dbcalendarapp.helper.CustomerHelper;
import com.prezstvn.dbcalendarapp.model.Country;
import com.prezstvn.dbcalendarapp.model.Customer;
import com.prezstvn.dbcalendarapp.model.Division;
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

public class ModifyCustomerController implements Initializable {
    public TextField CustomerId;
    public TextField CustomerName;
    public TextField CustomerAddress;
    public TextField PostalCode;
    public TextField Phone;
    public ComboBox<Country> CountryComboBox;
    public ComboBox<Division> DivisionComboBox;
    public Button ModifyCustomerSave;
    public Button CancelButton;

    private Customer customerToModify;

    public void onCountrySelection(ActionEvent actionEvent) throws SQLException {
        setDivisionComboBox(CountryComboBox.getValue().getCountryId());
    }

    private void setDivisionComboBox(int countryId) throws SQLException {
        DivisionComboBox.setItems(CustomerHelper.getDivisionListById(countryId));
    }

    /**
     * when save is clicked first we check to see if all fields meet Db requirements
     * once object is returned we set its id to our current customers
     * then pass it to our Customer query helper to update it on the DB
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            Customer updatedCustomer = isValidCustomer();
            updatedCustomer.setCustomerId(customerToModify.getCustomerId());
            CustomerHelper.updateCustomer(updatedCustomer);
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Records");
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch(CustomerCreationException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Creation Error:");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch(SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("DataBase Error:");
            alert.setContentText("Something went awry updating the Customer");
            alert.showAndWait();
        }
    }

    /**
     * all logical checks to ensure the user input values for customer are not empty then
     *
     * @return
     * @throws CustomerCreationException
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

    /**
     * is called from CustomerRecordController after an instance of this controller is loaded
     * sets the customerToModify then calls setFormFields() to set the visible form fields
     * @param customer sent from CustomerRecordController
     */
    public void setCustomerToModify(Customer customer) throws SQLException {
        customerToModify = customer;
        setFormFields();
    }

    /**
     * after customerToModify is sent from the controller
     * set all form fields to the values stored in customerToModify
     * also set CountryComboBox and DivisionComboBox
     */
    private void setFormFields() throws SQLException {
        CustomerId.setText(String.valueOf(customerToModify.getCustomerId()));
        CustomerName.setText(customerToModify.getCustomerName());
        CustomerAddress.setText(customerToModify.getAddress());
        PostalCode.setText(customerToModify.getAddress());
        Phone.setText(customerToModify.getPhone());
        //- 1 from countryId to match base index of CountryComboBox
        CountryComboBox.getSelectionModel().select(customerToModify.getCountryId()-1);
        setDivisionComboBox(customerToModify.getCountryId());
        setCustomerDivision();
    }

    /**
     * used to set initial division selection in DivisionComboBox
     * create and set observableList = items currently in combobox
     * created an iterator(could have written standard for loop isntead)
     * breaks when we find division in list
     * set selection to the index
     */
    private void setCustomerDivision() {
        ObservableList<Division> currentDivisionList = DivisionComboBox.getItems();
        int indexOfCustomerDivision = 0;
        for(Division div : currentDivisionList) {
            if(div.getDivisionId() == customerToModify.getDivisionId()) break;
            else indexOfCustomerDivision++;
        }
        DivisionComboBox.getSelectionModel().select(indexOfCustomerDivision);
    }

    /**
     * User is returned to the customerRecord scene
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/CustomerRecords.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Records");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    /**
     *  on controller creation set countrycombobox items to the returned values from .getCountryList()
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            CountryComboBox.setItems(CustomerHelper.getCountryList());

        } catch(SQLException e) {
            System.out.println(e);
        }
    }
}
