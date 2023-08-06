package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.LoginHelper;
import com.prezstvn.dbcalendarapp.logger.LoginLogger;
import com.prezstvn.dbcalendarapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    /**
     * field to hold user input username to login
     */
    public TextField userField;
    /**
     * field to hold user input password to login
     */
    public TextField passwordField;
    /**
     * to attempt login
     */
    public Button loginButton;
    /**
     * labeled to dynamically set the text to the users system language using the files in the resource bundle
     */
    public Label userLabel;
    /**
     * all fields are given fxid to dynamically assign text based on the language of the users system
     */
    public Label passwordLabel;

    public Label systemZoneId;
    /**
     * after login is succesfull this value is set to the user who logged in
     * to be passed to the MainView with the setUser(user) function
     */
    private User user;
    private String errorMessage;
    // used to hold the id of the user that has logged in, on order to send it to the MainViewController

    /**
     * takes input from the two textfields and attempts to login with this information
     * if login successful we log that info and push user to main menu
     * if not we log that info and alert the user that the attempt was a failure
     * @param actionEvent
     */
    public void loginAttempt(ActionEvent actionEvent) {
        try {
            String username = userField.getText();
            String password = passwordField.getText();
            user = LoginHelper.login(username, password);
            if(user != null) {
                System.out.println("YOU LOGGED IN");
                LoginLogger.logActivity(username, true);
                switchToMainScene(actionEvent);
            } else {
                throw new SQLException("The username and password combination you entered does not exist or is incorrect.");
            }
        } catch(SQLException e) {
            LoginLogger.logActivity(userField.getText(), false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed Login");
            alert.setContentText(errorMessage);
            alert.show();
        } catch(IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed to Load Resource");
            alert.setContentText("The resource is either in the wrong location or does not exist");
            alert.show();
        }
    }

    /**
     * switch scene to main menu
     * @param actionEvent
     * @throws IOException
     */
    private void switchToMainScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        controller.setUserId(user);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 191, 320));
        stage.show();
    }

    /**
     * dynamically set the fields in language based on the users ZoneId
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle bundle = ResourceBundle.getBundle("login", Locale.getDefault());
        ZoneId systemZone = ZoneId.systemDefault();
        systemZoneId.setText(systemZone.toString());
        userLabel.setText(bundle.getString("user"));
        passwordLabel.setText(bundle.getString("password"));
        loginButton.setText(bundle.getString("login"));
        errorMessage = bundle.getString("error");
    }
}
