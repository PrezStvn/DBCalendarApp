package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.exception.AppointmentException;
import com.prezstvn.dbcalendarapp.helper.AppointmentHelper;
import com.prezstvn.dbcalendarapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScheduleController implements Initializable {
    public RadioButton weekRadio;
    public ToggleGroup scheduleGroup;
    public RadioButton monthRadio;
    public TableView<Appointment> scheduleTable;
    public TableColumn<Appointment, Integer> appointmentIdColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descriptionColumn;
    public TableColumn<Appointment, String> locationColumn;
    public TableColumn<Appointment, Integer> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, ZonedDateTime> startDateColumn;
    public TableColumn<Appointment, ZonedDateTime> endDateColumn;
    public TableColumn<Appointment, Integer> customerIdColumn;
    public TableColumn<Appointment, Integer> userIdColumn;
    public Button addAppointmentButton;
    public Button modifyAppointmentButton;
    public Button deleteAppointmentButton;
    public Button MainMenu;

    private ObservableList<Appointment> appointmentSchedule;

    /**
     * gets values from the DB and then sets them in TableView scheduleTable
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            appointmentSchedule = AppointmentHelper.getAllAppointments();
            scheduleTable.setItems(appointmentSchedule);
            setSchedule();
        } catch (SQLException e) {
            System.out.println("something went wrong retrieving appointments from the DB.");
            throw new RuntimeException(e);
        }

    }

    /**
     * setting property values for entire TableView to properly display data returned from database
     */
    private void setSchedule() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));;
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));;
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));;
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));;
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("start"));;
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("end"));;
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));;
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));;
        }

    /**
     * currently running into an event propagation error that starts in this method
     * cause uknown at present
     *
     * @param actionEvent
     */
    public void onWeekSelect(ActionEvent actionEvent) {
        ZonedDateTime currDateTime = ZonedDateTime.now();
        // idea to use this was taken from stack overflow
        WeekFields weekFields = WeekFields.SUNDAY_START;
        int currWeek = currDateTime.get(weekFields.weekOfWeekBasedYear());
        int currYear = currDateTime.getYear();
        /**
         * LAMBDA: used to filter the currently seen schedule
         * essentially does what the for each loop below does but in somewhat less code
         */
        ObservableList<Appointment> weekView = appointmentSchedule.stream().filter(appt ->
                            /* getting the week of appt in appointmentSchedule then checking it against the users current week*/
                                        appt.getStart().get(weekFields.weekOfWeekBasedYear()) == currWeek &&
                                    /* getting year of appt in appointmentSchedule checking against the usersCurrentYear*/
                                        appt.getStart().getYear() == currYear)
                                    /*if both checks pass the appt is added to weekView */
                                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
        /*
        for(Appointment appt : weekView) {
            ZonedDateTime startTime = appt.getStart();
            int apptWeek = startTime.get(weekFields.weekOfWeekBasedYear());
            int apptYear = startTime.getYear();
            if(apptYear == currYear){
                if(apptWeek == currWeek) weekView.add(appt);
            }
        } */
        scheduleTable.setItems(weekView);
    }

    /**
     * LAMBDA: when Month view is selected, filter appointmentSchedule with a lambda expression
     *
     * @param actionEvent
     */
    public void onMonthSelect(ActionEvent actionEvent) {
        ZonedDateTime currDateTime = ZonedDateTime.now();
        int currMonth = currDateTime.getMonthValue();
        int currYear = currDateTime.getYear();
        //create an observableLIst monthview to hold appointmentSchedule appointments filtered by month.
        ObservableList<Appointment> monthView = appointmentSchedule.stream()
                /* retrieve month of the appt from appointmentSchedule, check against users current month */
                .filter(appt -> appt.getStart().getMonthValue() == currMonth &&
                        /* same with appt year then checking against users current year if both pass the appt is added to monthView*/
                        appt.getStart().getYear() == currYear).collect(Collectors.toCollection(FXCollections::observableArrayList));
        /*for(Appointment appt : monthView) {
            ZonedDateTime apptStartTime = appt.getStart();
            int apptMonth = apptStartTime.getMonthValue();
            int apptYear = apptStartTime.getYear();
            if(apptYear != currYear) monthView.remove(appt);
            if(apptMonth != currMonth) monthView.remove(appt);
        } */
        scheduleTable.setItems(monthView);
    }

    /**
     * Scene to AddAppointment
     * @param actionEvent
     * @throws IOException
     */
    public void addAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/AddAppointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * TableView scheduleTable selection model retireved
     * if no appointment is selected exception is thrown to exit code segment
     * if appointment is successfully deleted alert is shown with info about the deleted appointment apptId and appt Type
     * @param actionEvent
     */
    public void deleteAppointment(ActionEvent actionEvent) {
        try {
            Appointment appointmentToDelete = scheduleTable.getSelectionModel().getSelectedItem();
            if (appointmentToDelete == null) throw new AppointmentException("Please select an appointment to delete");
            AppointmentHelper.deleteAppointment(appointmentToDelete.getAppointmentId());
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Deletion Successful!");
            alert.setContentText("Appointment " + appointmentToDelete.getAppointmentId() + " of type: " + appointmentToDelete.getType() + " was successfully deleted.");
            alert.show();
            appointmentSchedule.remove(appointmentToDelete);
            scheduleTable.setItems(appointmentSchedule);
        } catch(AppointmentException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Appointment Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch(SQLException e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Customer Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * the currently selected appt is retrieved from selection model on tableview
     * begins to load the ModifyAppointment scene
     * after loader loads the file location we pull the instance of the class out to pass it the selected appointment with controller.setTargetAppointment()
     * if no appointment is selected in TableView an exception is thrown to exit the code segment and let the user know to select an appt
     * @param actionEvent
     * @throws IOException
     */
    public void modifyAppointment(ActionEvent actionEvent) throws IOException {
        try {
            Appointment targetAppointment = scheduleTable.getSelectionModel().getSelectedItem();
            if (targetAppointment == null) {
                throw new Exception("Please select an Appointment to modify.");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/prezstvn/dbcalendarapp/ModifyAppointment.fxml"));
            Parent root = loader.load();
            ModifyAppointmentController controller = loader.getController();
            controller.setTargetAppointment(targetAppointment);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch(Exception e) {
            Alert alert =  new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modify Appointment Selection Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Scene to Main Menu
     * @param actionEvent
     * @throws IOException
     */
    public void onMainMenuClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 191, 320));
        stage.show();
    }
}
