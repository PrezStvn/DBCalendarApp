package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.AppointmentException;
import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.helper.ContactHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import com.prezstvn.dbcalendarapp.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {
    public TextField AppointmentId;
    public TextField AppointmentTitle;
    public TextArea AppointmentDescription;
    public TextField AppointmentLocation;
    public Button AddAppointmentSave;
    public Button CancelButton;
    public TextField AppointmentCustomerId;
    public DatePicker AppointmentStartDate;
    public DatePicker AppointmentEndDate;
    public ComboBox<Contact> ContactComboCox;
    public TextField AppointmentUserId;
    public ComboBox<LocalTime> StartTimeComboBox;
    public ComboBox<LocalTime> EndTimeComboBox;
    public ComboBox<String> AppointmentTypeComboBox;

    /**
     * setting up combobox values when scene is created
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Contact> contactsCombo = ContactHelper.getContacts();
            ContactComboCox.setItems(contactsCombo);
            ContactComboCox.getSelectionModel().selectFirst();
            setTimes();
            setAppointmentTypes();
            AppointmentStartDate.setValue(LocalDate.now());
            AppointmentEndDate.setValue(LocalDate.now());
            StartTimeComboBox.getSelectionModel().select(LocalTime.of(8, 0));
            EndTimeComboBox.getSelectionModel().select(LocalTime.of(8, 30));
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Listing all 24 hours of the day in 15 min increments
     * cannot figure out a way to only display the window of business hours but in users time
     * presently beyond my capabilities
     */
    private void setTimes() {
        LocalTime startTime = LocalTime.of(0, 0);
        LocalTime endTime = LocalTime.of(23, 44);

        // Populate the ComboBox with time values in 15-minute increments
        while (startTime.isBefore(endTime) || startTime.equals(endTime)) {
            StartTimeComboBox.getItems().add(startTime);
            EndTimeComboBox.getItems().add(startTime);
            startTime = startTime.plusMinutes(15);
        }
    }

    /**
     * setting the selectable appointment Types
     * limits appointment types to only what is in comboBox
     */
    private void setAppointmentTypes() {
        ObservableList<String> typesList = FXCollections.observableArrayList();
        typesList.add("Planning Session");
        typesList.add("De-Briefing");
        typesList.add("Marketing");
        typesList.add("Coffee");
        AppointmentTypeComboBox.setItems(typesList);
    }

    /**
     * when the save Appt button is clicked this first executes logical checks to ensure user input fields are valid for an Appt object
     *
     * @param actionEvent SaveButton(AddAppointmentSave){horrible name} is clicked
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            //TODO: method that calls the save here
            Appointment appointmentToAdd = isValidAppointment();
            AppointmentHelper.createAppointment(appointmentToAdd);
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Schedule");
            stage.setScene(new Scene(root, 700, 600));
            stage.show();
        } catch(SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Appointment Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        catch(AppointmentException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Appointment Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     *
     * @return
     * @throws AppointmentException
     */
    private Appointment isValidAppointment() throws AppointmentException {
        Appointment appointmentToAdd =  new Appointment();
        //TODO: Logic checks mainly time constraints est 0800-2200
        String title = AppointmentTitle.getText();
        String description = AppointmentDescription.getText();
        String location = AppointmentLocation.getText();
        String type = AppointmentTypeComboBox.getValue();
        ZonedDateTime startTime = LocalDateTime.of(AppointmentStartDate.getValue(), StartTimeComboBox.getValue()).atZone(ZoneId.systemDefault());
        ZonedDateTime endTime = LocalDateTime.of(AppointmentEndDate.getValue(), EndTimeComboBox.getValue()).atZone(ZoneId.systemDefault());
        int customerId;
        int userId;
        int contactId = ContactComboCox.getSelectionModel().getSelectedItem().getContactId();
        try {
            customerId = Integer.parseInt(AppointmentCustomerId.getText());
            userId = Integer.parseInt(AppointmentUserId.getText());
        } catch(Exception e) {
            throw new AppointmentException("Customer_ID and User_ID most both be positive integers and not blank");
        }
        long timeCheck = ChronoUnit.MINUTES.between(startTime, endTime);
        if(timeCheck < 0) throw new AppointmentException("please select and End Time that is after the Start time.");
        long pastCheck = ChronoUnit.MINUTES.between(LocalDateTime.now(), startTime);
        if(pastCheck < 0) throw new AppointmentException("Please select a time that has not already passed.");
        if(type == null) throw new AppointmentException("Please select an appointment Type");
        if(title.equals("")) throw new AppointmentException("");
        if(description.equals("")) throw new AppointmentException("");
        if(location.equals("")) throw new AppointmentException("");
        ChronoCheck(customerId, startTime, endTime);
        appointmentToAdd.setTitle(title);
        appointmentToAdd.setDescription(description);
        appointmentToAdd.setLocation(location);
        appointmentToAdd.setType(type);
        appointmentToAdd.setStart(startTime);
        appointmentToAdd.setEnd(endTime);
        appointmentToAdd.setCustomerId(customerId);
        appointmentToAdd.setContactId(contactId);
        appointmentToAdd.setUserId(userId);
        return appointmentToAdd;
    }

    /**
     * all logic that checks times to ensure the new appointment has no time conflicts with any existing appointment
     * @param customerId passed in order to query for all appointments belonging to customer with customerId
     * @param startTime start time to check against
     * @param endTime to check against
     * @throws AppointmentException is thrown if one of the temporal checks fails carrying a message saying why
     */
    private void ChronoCheck(int customerId, ZonedDateTime startTime, ZonedDateTime endTime) throws AppointmentException {
        //setting the prescribed business hours to compare our start and end times against

        LocalTime businessStart = LocalTime.of(8, 0); // 08:00 EST
        LocalTime businessEnd = LocalTime.of(22, 0); // 22:00 EST
        //getting the selected times and changing them to est timezone
        ZonedDateTime estBusinessStart = startTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime estBusinessEnd = endTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        //converting the zoned times to localtimes in preparation for comparison
        LocalTime estBusStart = estBusinessStart.toLocalTime();
        LocalTime estBusEnd = estBusinessEnd.toLocalTime();
        //checking if start is between business hours then if end is between business hours EST
        if(!(estBusStart.isAfter(businessStart) && estBusStart.isBefore(businessEnd))) throw new AppointmentException("Appointment must start between the hours of  08:00 EST and 22:00 EST");
        if(!(estBusEnd.isAfter(businessStart) && estBusEnd.isBefore(businessEnd))) throw new AppointmentException("Appointment must end between the hours of  08:00 EST and 22:00 EST");

        try {
            ObservableList<Appointment> customerAppointments = AppointmentHelper.getCustomerAppointments(customerId);

            for(Appointment appt : customerAppointments) {
                //the new appts start time compared to the already schedule appts start time
                //if negative means new time > scheduled time.
                long newStartScheduledStart = ChronoUnit.MINUTES.between(startTime, appt.getStart());
                //new start in relation to scheduled end time
                long newStartScheduledEnd = ChronoUnit.MINUTES.between(startTime, appt.getEnd());
                //new end in relation to schedule start
                long newEndScheduledStart = ChronoUnit.MINUTES.between(endTime, appt.getStart());
                // new end in relation to scheduled end
                long newEndScheduledEnd = ChronoUnit.MINUTES.between(endTime, appt.getEnd());
                //if 2 appointments have the same start time
                if(newStartScheduledStart == 0) throw new AppointmentException("Scheduling conflict: two appointments cannot start at the same time.");
                //if 2 appointments have the same end time
                if(newEndScheduledEnd == 0) throw new AppointmentException("Scheduling conflict: two appointments cannot end at the same time");
                //if new appt starts during another appointment
                if(newStartScheduledStart < 0 && newStartScheduledEnd > 0) throw new AppointmentException("Scheduling conflict: this start time is during another scheduled appointment for this Customer.");
                //if new appt ends during another sheduled meeting.
                if(newEndScheduledStart < 0 && newEndScheduledEnd > 0) throw new AppointmentException("Scheduling conflict: the selected end time is during an already scheduled appointment for this Customer.");
                //if new appt has another scheduled meeting happen during its times(this meeting is so long it encompasses another meeting)
                if(newStartScheduledStart > 0 && newEndScheduledEnd < 0) throw new AppointmentException("Scheduling conflict: this customer has an appointment scheduled during these times already.");
            }
        } catch (SQLException e) {
            throw new AppointmentException("Sql exception thrown, most likely the given customer id does not exist");
        }
    }

    /**
     * Scene is switched back to Schedule and no other actions are taken
     * @param actionEvent
     * @throws IOException
     */
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }


}
