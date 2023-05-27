package com.prezstvn.dbcalendarapp;

import com.prezstvn.dbcalendarapp.helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CalendarApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalendarApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 360);
        stage.setTitle("Login");
        stage.setScene(scene);
        JDBC.openConnection();
        stage.setOnCloseRequest(windowEvent -> {
            JDBC.closeConnection();
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}