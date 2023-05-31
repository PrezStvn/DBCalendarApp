package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

public abstract class AppointmentHelper {

    public static Appointment getAppointment(int appointmentId) throws SQLException {
        Appointment retrievedAppointment;
        String sql = "SELECT Appointment_ID, Title, Description, Location, Contact_ID, " +
                "Type, Start, End, Customer_ID, User_ID FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt( 1, appointmentId);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            retrievedAppointment =  mapAppointment(rs);
        } else {
            throw new SQLException("No Appointment was retrieved");
        }
        return retrievedAppointment;
    }

    /**
     * Primarily used to set all scheduled appts on scene initialized
     * @return allAppointments = all appointments returned from the DB
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Contact_ID, " +
                "Type, Start, End, Customer_ID, User_ID FROM APPOINTMENTS;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Appointment appt = mapAppointment(rs);
            allAppointments.add(appt);
        }
        return allAppointments;
    }

    /**
     * used to get all scheduled appts belonging to customerId
     * used to check times and ensure there are no scheduling conflicts
     * @param customerId
     * @return list of customerId's scheduled appts
     * @throws SQLException will only be thrown if there is an issue with the DB
     */
    public static ObservableList<Appointment> getCustomerAppointments(int customerId) throws SQLException {
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Appointments WHERE Customer_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Appointment appt = mapAppointment(rs);
            customerAppointments.add(appt);
        }
        return customerAppointments;
    }

    /**
     * takes the data returned from the database and maps it to a local Appointment object
     * @param rs the ResultSet returned from any query that returns an or many Appointments
     * @return mappedAppointment the newly mapped appointment
     * @throws SQLException
     */
    private static Appointment mapAppointment(ResultSet rs) throws SQLException {
        ZoneId thisZoneId = ZoneId.systemDefault();
        Appointment mappedAppointment = new Appointment();
        mappedAppointment.setAppointmentId(rs.getInt("Appointment_ID"));
        mappedAppointment.setTitle(rs.getString("Title"));
        mappedAppointment.setDescription(rs.getString("Description"));
        mappedAppointment.setLocation(rs.getString("Location"));
        mappedAppointment.setContactId(rs.getInt("Contact_ID"));
        mappedAppointment.setType(rs.getString("Type"));
        //TODO: times presently show both utc and local  times are displaying as intended now i think

        LocalDateTime startDate = rs.getTimestamp("Start").toLocalDateTime();
        ZonedDateTime zonedStartDate = ZonedDateTime.of(startDate, thisZoneId);
        mappedAppointment.setStart(zonedStartDate);
        LocalDateTime endDate = rs.getTimestamp("End").toLocalDateTime();
        ZonedDateTime zonedEndDate = ZonedDateTime.of(endDate, thisZoneId);
        mappedAppointment.setEnd(zonedEndDate);
        mappedAppointment.setCustomerId(rs.getInt("Customer_ID"));
        mappedAppointment.setUserId(rs.getInt("User_ID"));
        return mappedAppointment;
    }

    public static void createAppointment(Appointment appointmentToAdd) throws SQLException {
        Timestamp startTime = Timestamp.from(appointmentToAdd.getStart().toInstant());
        Timestamp endTime = Timestamp.from(appointmentToAdd.getEnd().toInstant());
        String sql =    "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointmentToAdd.getTitle());
        ps.setString(2, appointmentToAdd.getDescription());
        ps.setString(3, appointmentToAdd.getLocation());
        ps.setString(4, appointmentToAdd.getType());
        ps.setTimestamp(5, startTime);
        ps.setTimestamp(6, endTime);
        ps.setInt(7, appointmentToAdd.getCustomerId());
        ps.setInt(8, appointmentToAdd.getUserId());
        ps.setInt(9, appointmentToAdd.getContactId());
        int rowAffected = ps.executeUpdate();
        if(rowAffected == 0) {
            throw new SQLException("No Appointment was created, internal error");
        }
    }

    public static void deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentId);
        int rowsAffected = ps.executeUpdate();
        if(rowsAffected == 0) throw new SQLException("Nothing was deleted");
    }

    public static void updateAppointment(Appointment updatedAppointment) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID, User_ID, Contact_ID " +
                        "WHERE Appointment_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        Timestamp startTime = Timestamp.from(updatedAppointment.getStart().toInstant());
        Timestamp endTime = Timestamp.from(updatedAppointment.getEnd().toInstant());
        ps.setString(1, updatedAppointment.getTitle());
        ps.setString(2, updatedAppointment.getDescription());
        ps.setString(3, updatedAppointment.getLocation());
        ps.setString(4, updatedAppointment.getType());
        ps.setTimestamp(5, startTime);
        ps.setTimestamp(6, endTime);
        ps.setInt(7, updatedAppointment.getCustomerId());
        ps.setInt(8, updatedAppointment.getUserId());
        ps.setInt(9, updatedAppointment.getContactId());
        int rowAffected = ps.executeUpdate();
        if(rowAffected == 0) {
            throw new SQLException("No Appointment was updated, internal error");
        }
    }

    /**
     * query and return all appointments associated with userId(User_ID on db)
     * @param userId
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> getUserAppointments(int userId) throws SQLException {
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Appointments WHERE Customer_ID =?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Appointment appt = mapAppointment(rs);
            userAppointments.add(appt);
        }
        return userAppointments;
    }
}
