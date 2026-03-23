/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorraineb, seany
 */
public class DatabaseConnection {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/peso_track"
             + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Manila";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    
    public static Connection getConnection() throws SQLException {
        try{
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Failed to connect to database", e);
            throw e;
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isConnectionValid = conn != null && conn.isValid(3);
            return isConnectionValid;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Database connection test failed", e);
            return false;
        }
    }
}
