package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.Country;
import com.prezstvn.dbcalendarapp.model.Customer;
import com.prezstvn.dbcalendarapp.model.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public static ObservableList<Country> getCountryList() throws SQLException {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        String sql = "select * from countries;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            countryList.add(new Country(rs.getInt("Country_ID"), rs.getString("Country")));
        }
        return countryList;
    }

    /**
     * can overload Division's constructor and remove requirement for countryId for this version of retrieving divisions
     * @param countryId id used to query Divisions on the DB by
     * @return ObservableList<Division> of divisions belonging to the country with this countryID
     * @throws SQLException if the DataBase returns some kind of error
     */
    public static ObservableList<Division> getDivisionListById(int countryId) throws SQLException {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        String sql = "select * from first_level_divisions where Country_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt( 1, countryId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            divisionList.add(new Division(rs.getInt("Division_ID"), rs.getString("Division"), rs.getInt("Country_ID")));
        }
        return divisionList;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public static ObservableList<Division> getDivisionList() throws SQLException {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        String sql = "select * from first_level_divisions;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            divisionList.add(new Division(rs.getInt("Division_ID"), rs.getString("Division"), rs.getInt("Country_ID")));
        }
        return divisionList;
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

    public static void updateCustomer(Customer customerToModify) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, " +
                        "Postal_Code = ?, Phone = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerToModify.getCustomerName());
        ps.setString(2, customerToModify.getAddress());
        ps.setString(3, customerToModify.getPostalCode());
        ps.setString(4, customerToModify.getPhone());
        ps.setInt(5, customerToModify.getDivisionId());
        ps.setInt(6, customerToModify.getCustomerId());

        int rowsAffected = ps.executeUpdate();
        if(rowsAffected == 0) {
            throw new SQLException("No Rows affected Sql Update Failed!");
        }
    }

    public static void createCustomer(Customer createdCustomer) throws SQLException {
        String sql = "INSERT INTO customers(Customer_name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES(?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, createdCustomer.getCustomerName());
        ps.setString(2, createdCustomer.getAddress());
        ps.setString(3, createdCustomer.getPostalCode());
        ps.setString(4, createdCustomer.getPhone());
        ps.setInt(5, createdCustomer.getDivisionId());
        int affectedRows = ps.executeUpdate();
        if(affectedRows == 0) {
            throw new SQLException("SQL Insert Failed");
        }
    }

    public static void deleteCustomer(Customer targetCustomer) throws SQLException {
        String sqlSchedule = "DELETE FROM Appointments WHERE Customer_ID = ?";
        PreparedStatement psSchedule = JDBC.connection.prepareStatement(sqlSchedule);
        psSchedule.setInt(1, targetCustomer.getCustomerId());
        int rowsAffected = psSchedule.executeUpdate();
        System.out.println(rowsAffected + " rows were affected");
        String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, targetCustomer.getCustomerId());
        rowsAffected = ps.executeUpdate();
        if(rowsAffected == 0) throw new SQLException("No Customer was Deleted");
    }


}
