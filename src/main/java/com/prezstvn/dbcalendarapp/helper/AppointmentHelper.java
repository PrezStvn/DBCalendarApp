package com.prezstvn.dbcalendarapp.helper;

import com.prezstvn.dbcalendarapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        //TODO: times presently show both utc and local  idk whats up with that || still need to find that kinkead video
        LocalDateTime startDate = rs.getTimestamp("Start").toLocalDateTime();
        ZonedDateTime zonedStartDate = ZonedDateTime.of(startDate, thisZoneId);
        mappedAppointment.setStart(zonedStartDate);
        LocalDateTime endDate = rs.getTimestamp("End").toLocalDateTime();
        ZonedDateTime zonedEndDate = ZonedDateTime.of(startDate, thisZoneId);
        mappedAppointment.setEnd(zonedEndDate);
        mappedAppointment.setCustomerId(rs.getInt("Customer_ID"));
        mappedAppointment.setUserId(rs.getInt("User_ID"));
        return mappedAppointment;
    }
}
