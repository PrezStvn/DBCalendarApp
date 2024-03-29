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

/**
 * FUTURE ENHANCEMENT: it may be more intelligent to combine both the controllers and the views for
 * add and modify CustomerRecords(also for Appointments) the functionality and style of both pages are near identical
 * both modify and create take in all the same info just a logical choice would need to be made at page creation
 * to set page as add Customer or Modify Customer
 *
 */

public class ModifyAppointmentController implements Initializable {
    /**
     * all attributes are the same as AddAppointmentController
     *
     */
    public TextField AppointmentId;

    public TextField AppointmentTitle;
    public TextField AppointmentLocation;
    public TextField AppointmentCustomerId;
    public Button ModifyAppointmentSave;
    public Button CancelButton;
    public DatePicker AppointmentStartDate;
    public DatePicker AppointmentEndDate;
    public ComboBox<Contact> ContactComboCox;
    public TextField AppointmentUserId;
    public TextArea AppointmentDescription;
    public ComboBox<String> AppointmentTypeComboBox;

    private Appointment targetAppointment;
    public ComboBox<LocalTime> StartTimeComboBox;
    public ComboBox<LocalTime> EndTimeComboBox;

    /**
     * initialize to populate contactcombobox with contacts from the DB
     * then to setTimes()
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
            AppointmentStartDate.setValue(LocalDate.now());
            AppointmentEndDate.setValue(LocalDate.now());
            StartTimeComboBox.getSelectionModel().select(LocalTime.of(8, 0));
            EndTimeComboBox.getSelectionModel().select(LocalTime.of(8, 30));
        } catch(SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * attempt to save Appointment with saveAppointmentLogicCheck()
     * if successful we do some housekeeping and reset static targetAppointment to null
     * may not be necessary but seems advisable
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        try {
            Appointment updatedAppointment = isValidAppointment();
            AppointmentHelper.updateAppointment(updatedAppointment);
            Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Schedule");
            stage.setScene(new Scene(root, 900, 400));
            stage.show();
        } catch(AppointmentException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch(SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * setting the time selection combo boxes to have every 15 minute increment of every hour
     * could not logically figure out how to get every 15 min interval since the last iteration would bring the value to 0
     * could add manually after the while exits
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
        StartTimeComboBox.getItems().add(LocalTime.of(23, 45));
        EndTimeComboBox.getItems().add(LocalTime.of(23, 45));
    }

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

    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/Schedule.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Schedule");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }

    /**
     * used to pass the selected appointment to modify from the schedule view to here after this instance of the controller is loaded
     * @param sentAppointment
     */
    public void setTargetAppointment(Appointment sentAppointment) {
        targetAppointment = sentAppointment;
        setFormFields();
    }

    /**
     * setting all forms initial values based on those in targetAppointment
     * TODO: add field for the current time as well maybe another combo with 15 min intervals;
     */
    private void setFormFields() {
        AppointmentId.setText(String.valueOf(targetAppointment.getAppointmentId()));
        AppointmentTitle.setText(targetAppointment.getTitle());
        AppointmentDescription.setText(targetAppointment.getDescription());
        AppointmentLocation.setText(targetAppointment.getLocation());
        AppointmentStartDate.setValue(targetAppointment.getStart().toLocalDate());
        AppointmentEndDate.setValue(targetAppointment.getEnd().toLocalDate());
        AppointmentCustomerId.setText(String.valueOf(targetAppointment.getCustomerId()));
        setContactComboBox();
        setAppointmentTypes();
        AppointmentTypeComboBox.getSelectionModel().select(targetAppointment.getType());
        AppointmentUserId.setText(String.valueOf(targetAppointment.getUserId()));
    }

    /**
     * setting types in the type combobox
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
     * iterating through contacts in the contactcomboBox
     * once matching contact is found the index is returned then
     * that index is used to set the value displayed in the ContactComboBox
     */
    private void setContactComboBox() {
        ObservableList<Contact> contactList = ContactComboCox.getItems();
        int indexOfContactInList = 0;
        for(Contact contact : contactList) {
            if(contact.getContactId() == targetAppointment.getContactId()) break;
            else indexOfContactInList++;
        }
        ContactComboCox.getSelectionModel().select(indexOfContactInList);
    }

    /**
     * all logical time checks
     * first checks to ensure times selected are during normal business hours 0800-2200 EST
     * then checks against all other customer_id appts to see if there are any overlaps
     * throws an exception if any checks fail
     * @param customerId customer for whom we are scheduling an appt
     * @param startTime the soon to be appts start time
     * @param endTime "" end time
     * @throws AppointmentException
     */
    private void ChronoCheck(int customerId, ZonedDateTime startTime, ZonedDateTime endTime) throws AppointmentException {
        LocalTime businessStart = LocalTime.of(8, 0); // 08:00 EST
        LocalTime businessEnd = LocalTime.of(22, 0); // 22:00 EST
        ZonedDateTime estBusinessStart = startTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime estBusinessEnd = endTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalTime estBusStart = estBusinessStart.toLocalTime();
        LocalTime estBusEnd = estBusinessEnd.toLocalTime();
        if(!(estBusStart.isAfter(businessStart) && estBusStart.isBefore(businessEnd))) throw new AppointmentException("Appointment must start between the hours of  08:00 EST and 22:00 EST");
        if(!(estBusEnd.isAfter(businessStart) && estBusEnd.isBefore(businessEnd))) throw new AppointmentException("Appointment must end between the hours of  08:00 EST and 22:00 EST");

        try {
            ObservableList<Appointment> customerAppointments = AppointmentHelper.getCustomerAppointments(customerId);

            for(Appointment appt : customerAppointments) {
                //skips checks on the unupdated version of this appointment that is still stored in customerAppointments
                if(appt.getAppointmentId() == targetAppointment.getAppointmentId()) continue;
                //the new appts start time compared to the scheduled appts start time
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
            //is thrown if our call to
            throw new AppointmentException("Sql exception thrown, most likely the given customer id does not exist");
        }
    }
}
