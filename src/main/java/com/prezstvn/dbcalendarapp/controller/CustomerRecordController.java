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
    public Button addCustomerButton;
    public Button deleteCustomerButton;
    public Button modifyCustomerButton;

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

        try {
            Customer targetCustomer = customerRecordTable.getSelectionModel().getSelectedItem();
            if (targetCustomer == null) {
                throw new Exception("Please select a Customer Record to delete");
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setContentText("Do you wish to delete this Customer and their currently scheduled Appointments?");
            ButtonType confirmButton = ButtonType.YES;
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(confirmButton, cancelButton);
            /*  I do not know how poor of practice it is to include exception handling in the expression like this
            *  Lambda to take user input on the alert shown before user deletes customer
            *
             */
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == confirmButton) {
                    try {
                        CustomerHelper.deleteCustomer(targetCustomer);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else if (buttonType == cancelButton) alert.close();
            });
            updateCustomerTable();
        } catch(Exception e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Customer Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            }
    }

    private void updateCustomerTable() throws SQLException {
        customerRecords = CustomerHelper.getCustomerRecords();
        customerRecordTable.setItems(customerRecords);
    }

    public void modifyCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            Customer targetCustomer = customerRecordTable.getSelectionModel().getSelectedItem();
            if(targetCustomer == null) {
                throw new Exception("Please select a Customer Record to modify");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/prezstvn/dbcalendarapp/ModifyCustomer.fxml"));
            Parent root = loader.load();
            ModifyCustomerController controller = loader.getController();
            controller.setCustomerToModify(targetCustomer);
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
    }

    public void onMainMenuClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
}
