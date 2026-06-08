package com.spaceshield;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    
    private static final String URL = "jdbc:h2:file:./database/spaceshieldDB;INIT=RUNSCRIPT FROM './database/database.sql'";
    
    private static final String USER = "sa"; 
    private static final String PASSWORD = ""; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
