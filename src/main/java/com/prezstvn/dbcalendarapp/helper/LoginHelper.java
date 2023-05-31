package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LoginHelper {

    public static User login(String username, String password) throws SQLException {
        User user;
        String sql = "SELECT * from USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            user = new User(rs.getInt("User_ID"), rs.getString("User_Name"));
        } else {
            throw new SQLException("unsuccessful login attempt");
        }
        return user;
    }

    /**
     * used only in custom report to grab list of users and display them in a combobox
     * @return
     * @throws SQLException
     */
    public static ObservableList<User> getListOfUsers() throws SQLException {
        ObservableList<User> userList = FXCollections.observableArrayList();
        String sql = "SELECT User_ID, User_Name FROM USERS;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            userList.add(new User(rs.getInt("User_ID"), rs.getString("User_Name")));
        }
        return userList;
    }
}
