package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerHelper {

    public static ObservableList<Customer> getCustomerRecords() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sql =    "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, fld.Division_ID, co.Country_ID " +
                        "FROM client_schedule.customers AS c " +
                        "JOIN first_level_divisions AS fld on c.Division_ID = fld.Division_ID " +
                        "JOIN countries AS co on fld.Country_ID = co.Country_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Customer newCustomer = mapCustomer(rs);
            allCustomers.add(newCustomer);
        }
        return allCustomers;
    }

    private static Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer mappedCustomer = new Customer();
        mappedCustomer.setCustomerId(rs.getInt("Customer_ID"));
        mappedCustomer.setCustomerName(rs.getString("Customer_Name"));
        mappedCustomer.setAddress(rs.getString("Address"));
        mappedCustomer.setPostalCode(rs.getString("Postal_Code"));
        mappedCustomer.setPhone(rs.getString("Phone"));
        mappedCustomer.setDivisionId(rs.getInt("Division_ID"));
        mappedCustomer.setCountryId(rs.getInt("Country_ID"));
        return mappedCustomer;
    }
}
