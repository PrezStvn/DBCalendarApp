DBCalendar Application
    Desktop application used for viewing, creating, updating, deleting scheduled appointments.
    All data is stored externally on a MySQL database


AUTHOR:
    Steven Perez
    Email: sper314@wgu.edu
    personal: prez.stvn@gmail.com
    application version: 1.0.0
    Date: 31May2023

DEPENDENCIES
    IntelliJ IDEA 2022.3 (Community Edition)
    JDK-17 Java SE 17.0.5.0
    JavaFX-SDK-17.0.2
    mysql-connector-java-8.0.33

Running Program:
    At filepath DBCalendarApp/DB you will find 4 files the first two are the database creation files
    running those while having mysql server installed will create a local version of the Database
    the third file is the connection class to connect to the server you just created
    the final file links is just a link to where you can download the mysql connector please select mysql-connector-java-8.0.33
    After this has all been setup you should be able to click on the CalendarApp main class and run the program so long as your local Databaser is up and running.

Additional Report:
    The custom report I came up with with a view of each users appointments
    So another scene with a comboBox and a list of users inside based on which user you select
    all appointments for that user will be shown