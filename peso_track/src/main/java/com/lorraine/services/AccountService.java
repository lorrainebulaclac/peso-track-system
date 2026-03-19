/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.services;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.lorraine.config.DatabaseConnection;
import com.lorraine.models.AccountModel;
import com.lorraine.models.AccountType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 *
 * @author lorraineb, seany
 */
public class AccountService {
    
    private static final Logger LOGGER = Logger.getLogger(AccountService.class.getName());
    
    
public List<AccountModel> getAccountsByUser(long userID) {
        String query = "SELECT id, user_id, type, balance "
                + "FROM accounts "
                + "WHERE user_id = ? "
                + "ORDER BY type ASC";
        
        List<AccountModel> accounts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    accounts.add(mapAccount(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get Accounts by User" + e.getMessage(), e);
        }
        return accounts;
    }
    
    private AccountModel mapAccount(ResultSet rs) throws SQLException {
        return new AccountModel(
                rs.getLong("id"),
                rs.getLong("user_id"),
                AccountType.fromValue(rs.getString("type")),
                rs.getBigDecimal("balance")
        );
    }
}