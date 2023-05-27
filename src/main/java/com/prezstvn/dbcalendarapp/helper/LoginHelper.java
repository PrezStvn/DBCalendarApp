package com.prezstvn.dbcalendarapp.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LoginHelper {

    public static boolean login(String username, String password) throws SQLException {
        boolean loggedIn = false;
        String sql = "SELECT User_ID from USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            loggedIn = true;
        }
        return loggedIn;
    }
}
