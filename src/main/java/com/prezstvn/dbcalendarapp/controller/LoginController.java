package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.LoginHelper;
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
    public TextField userField;
    public TextField passwordField;
    public Button loginButton;
    public Label userLabel;
    public Label passwordLabel;
    public Label systemZoneId;
    private User user;
    private String errorMessage;
    // used to hold the id of the user that has logged in, on order to send it to the MainViewController

    public void loginAttempt(ActionEvent actionEvent) {
        try {
            String username = userField.getText();
            String password = passwordField.getText();
            user = LoginHelper.login(username, password);
            if(user != null) {
                System.out.println("YOU LOGGED IN");
                switchToMainScene(actionEvent);
            } else {
                throw new SQLException("The username and password combination you entered does not exist or is incorrect.");
            }
        } catch(SQLException e) {
            System.out.println(e);
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

    private void switchToMainScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Parent root = loader.load();
        MainViewController controller = loader.getController();
        controller.setUserId(user);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

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
