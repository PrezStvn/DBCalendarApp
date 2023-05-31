package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.User;

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
}
