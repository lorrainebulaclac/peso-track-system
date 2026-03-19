/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.services;

import com.lorraine.models.UserModel;
import com.lorraine.config.DatabaseConnection;
import com.lorraine.models.AuthenticationResult;
import com.lorraine.models.LoginResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author lorraineb, seany
 */
public class AuthenticationService {
    
    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());
    private static final int MYSQL_DUPLICATE_KEY = 1062;
    private static final String[] DEFAULT_ACCOUNT_TYPES = {
        "Cash", "Savings", "Credit Card", "Other"
    };
    
    // Register
    
    public AuthenticationResult registerUser(String firstName, String lastName, String email, String password) {
        try (Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false);
            
            try {
                String hashedPassword = hashPassword(password);
                long userID = insertUser(conn, firstName, lastName, email, hashedPassword);
                insertDefaultAccounts(conn, userID);
                conn.commit();
                
                LOGGER.log(Level.INFO, "User registered successfully - {0}", email);
                return AuthenticationResult.success("User registered successfully");
            } catch (SQLException e){
                conn.rollback();
                return handleRegistrationError(e, email);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Database connection error during registration", e);
            return AuthenticationResult.failed("Could not connect to the database");
        }
    }
    
    // Login
    
    public LoginResult loginUser(String email, String password) {
        UserModel user = getUserByEmail(email);
        
        if (user == null) {
            LOGGER.log(Level.WARNING, "Login failed: User not found - {0}", email);
            return LoginResult.failed("Invalid email or password");
        }
        
        if (!verifyPassword(password, user.getPassword())) {
            LOGGER.log(Level.WARNING, "Login failed: Incorrect password - {0}", email);
            return LoginResult.failed("Invalid email or password");
        } 
        
        if (!hasCompletedAccounts(user.getUserID())) {
            LOGGER.log(Level.SEVERE, "Login blocked: Incomplete accounts for user_id - {0}", user.getUserID());
            return LoginResult.failed("Your account setup is incomplete. Please contact support.");
        }
        
        LOGGER.log(Level.INFO, "User logged in successfully - {0}", email);
            return LoginResult.success("Login successful", user);
    }
    
    // Get UserModel From Database
    
    public UserModel getUserByEmail(String email) {
        String query = "SELECT user_id, first_name, last_name, email, password, created_at "
                + "FROM users "
                + "WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user from database", e);
        }
        return null;
    }
    
    private long insertUser(Connection conn,
                    String firstName, String lastName, 
                    String email, String hashedPassword)  throws SQLException {
        
        String query = "INSERT INTO users (first_name, last_name, email, password) "
                + "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next())
                    return keys.getLong(1);
                throw new SQLException("Insert succeded but no generated key was returned");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting user into database", e);
            throw e;
        }
    }
    
    private void insertDefaultAccounts(Connection conn, long userID) throws SQLException {
        String query = "INSERT INTO accounts (user_id, type) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (String type : DEFAULT_ACCOUNT_TYPES) {
                stmt.setLong(1, userID);
                stmt.setString(2, type);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting default accounts into database", e);
            throw e;
        }
    }
    
    private boolean hasCompletedAccounts(long userID){
        String query = "SELECT COUNT(*) FROM accounts WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){
            
            stmt.setLong(1, userID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1) == DEFAULT_ACCOUNT_TYPES.length;
            }
        } catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Error checking accounts for user_id = {0}", userID);
        }
        return false;
    }
    
    private UserModel mapUser (ResultSet rs) throws SQLException {
        return new UserModel(
            rs.getLong("user_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getDate("created_at").toLocalDate() 
        );
    }
    
    private AuthenticationResult handleRegistrationError (SQLException e, String email) {
        if (e.getErrorCode() == MYSQL_DUPLICATE_KEY) {
            LOGGER.log(Level.WARNING, "Registration failed: email already exists - {0}", email);
            return AuthenticationResult.failed("Email already exists");
        }
        LOGGER.log(Level.SEVERE, "Registration failed, registration rolled back", e);
            return AuthenticationResult.failed("Registration failed. Please try again.");
    }
    
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
