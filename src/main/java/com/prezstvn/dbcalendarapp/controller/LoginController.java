package com.prezstvn.dbcalendarapp.controller;

import com.prezstvn.dbcalendarapp.helper.LoginHelper;
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
    public AnchorPane zoneId;
    public Label userLabel;
    public Label passwordLabel;
    public Label systemZoneId;

    private String errorMessage;

    public void loginAttempt(ActionEvent actionEvent) {
        try {
            String username = userField.getText();
            String password = passwordField.getText();
            if(LoginHelper.login(username, password)) {
                System.out.println("YOU LOGGED IN");
                switchToMainScene(actionEvent);
            } else {
                throw new SQLException();
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
        Parent root = FXMLLoader.load(getClass().getResource("/com/prezstvn/dbcalendarapp/MainMenu.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Main App Menu");
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
