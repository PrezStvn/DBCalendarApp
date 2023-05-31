package com.prezstvn.dbcalendarapp.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class LoginLogger {
    //root filepath to write to and read from
    private static final String FILENAME = "login_activity.txt";

    /**
     * immediately call createFileIfNotExists() to create the txt document wherever this application is run.
     * this means that data must exist by the time a user can view it
     * this report should never be empty
     * first we log the time in format yyyy-MM-dd HH:mm:ss
     * then using the passed in boolean ternary is used to create string for success or failure
     * create format for sting output and insert preassigned variables.
     * @param username the name a user attempted to login with
     * @param success if the login attempt was successful or not
     */
    public static void logActivity(String username, boolean success) {
        createFileIfNotExists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, true))) {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String status = success ? "Success" : "Failure";
            String logEntry = String.format("%s - User: %s, Status: %s", timeStamp, username, status);
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * see if file exists at path
     * creates file if it does not exist
     */
    private static void createFileIfNotExists() {
        Path path = Paths.get(FILENAME);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
