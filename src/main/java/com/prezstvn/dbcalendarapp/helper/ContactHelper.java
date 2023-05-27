package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class ContactHelper {
    public static ObservableList<Contact> getContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        Map<String, Integer> contactMap = new HashMap<String, Integer>();
        String sql = "SELECT Contact_ID, Contact_Name FROM Contacts;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Contact newContact = new Contact();
            newContact.setContactId(rs.getInt("Contact_ID"));
            newContact.setContactName(rs.getString("Contact_Name"));
            contactMap.put(newContact.getContactName(), newContact.getContactId());
            allContacts.add(newContact);
        }

        return allContacts;
    }
}
