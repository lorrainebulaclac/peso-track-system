/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.services;

import com.lorraine.config.DatabaseConnection;
import com.lorraine.models.AccountType;
import com.lorraine.models.TransactionModel;
import com.lorraine.models.TransactionType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorraineb, seany
 */
public class TransactionService {
    
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    
    // Add TransactionModel
    public boolean addTransaction(long user_id, long account_id, long category_id, 
            TransactionType type, BigDecimal amount, LocalDate date, String description) {
        
        String insertSQL = "INSERT INTO transactions "
                + "(user_id, account_id, category_id, type, amount, date, description) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        String balanceSQL = type == TransactionType.INCOME 
                ? "UPDATE accounts SET balance = balance + ? WHERE id = ?" 
                : "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try(PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                    PreparedStatement balanceStmt = conn.prepareStatement(balanceSQL)) {
                
                insertStmt.setLong(1, user_id);
                insertStmt.setLong(2, account_id);
                insertStmt.setLong(3, category_id);
                insertStmt.setString(4, type.getValue());
                insertStmt.setBigDecimal(5, amount);
                insertStmt.setDate(6, Date.valueOf(date));
                insertStmt.setString(7, description);
                insertStmt.executeUpdate();
                
                if (type != TransactionType.TRANSFER) {
                    balanceStmt.setBigDecimal(1, amount);
                    balanceStmt.setLong(2, account_id);
                    balanceStmt.executeUpdate();
                }
                
                conn.commit();
                LOGGER.log(Level.INFO, "Transaction added for user_id={0}", user_id);
                return true;
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Transaction failed, rolled back", e);
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to database", e);
            return false;
        }
    }
    
    // Edit TransactionModel
    public boolean editTransaction(long transactionId, long account_id, long categoryId,
                                    TransactionType type, BigDecimal amount,
                                    LocalDate date, String description) {
        
        String sql = "UPDATE transactions " +
                     "SET account_id = ?, category_id = ?, type = ?, amount = ?, date = ?, description = ? " +
                     "WHERE id = ? LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, account_id);
            stmt.setLong(2, categoryId);
            stmt.setString(3, type.getValue());
            stmt.setBigDecimal(4, amount);
            stmt.setDate(5, java.sql.Date.valueOf(date));
            stmt.setString(6, description);
            stmt.setLong(7, transactionId);
            stmt.executeUpdate();
            LOGGER.log(Level.INFO, "Transaction updated id={0}", transactionId);
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating transaction", e);
            return false;
        }
    }
    
    // Delete TransactionModel
    public boolean deleteTransaction(long transaction_id) {
        
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, transaction_id);
            stmt.executeUpdate();
            LOGGER.log(Level.INFO, "Transaction deleted id={0}", transaction_id);
            return true;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting transaction", e);
            return false;
        }
    }
    
    // Search Transactions
    public List<TransactionModel> searchTransactions(
            long user_id, String description, 
            String accountName, String categoryName, 
            TransactionType type, LocalDate date) {
        
        StringBuilder sql = new StringBuilder(
                "SELECT t.id, t.user_id, t.account_id, t.category_id, "
                + "c.name AS category_name, a.type AS account_type, "
                + "t.type, t.amount, t.date, t.description "
                + "FROM transactions AS t "
                + "LEFT JOIN categories AS c ON t.category_id = c.id "
                + "LEFT JOIN accounts AS a ON t.account_id = a.id "
                + "WHERE t.user_id = ? "
        );
        
        List<Object> params = new ArrayList<>();
        params.add(user_id);
        
        if (description != null && !description.isBlank()) {
            sql.append("AND t.description LIKE ? ");
            params.add("%" + description + "%");
        }
        if (accountName != null && !accountName.isBlank()) {
            sql.append("AND a.type LIKE ? ");
            params.add("%" + accountName + "%");
        }
        if (categoryName != null && !categoryName.isBlank()) {
            sql.append("AND c.name LIKE ? ");
            params.add("%" + categoryName + "%");
        }
        if (type != null) {
            sql.append("AND t.type = ? ");
            params.add(type.getValue());
        }
        if (date != null) {
            sql.append("AND t.date = ? ");
            params.add(Date.valueOf(date));
        }

        sql.append("ORDER BY t.date DESC");
        
        List<TransactionModel> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching transactions", e);
        }
        return transactions;
    }
    
    // Get All Transactions by User
    public List<TransactionModel> getTransactionsByUser(long user_id) {
        String sql = "SELECT t.id, t.user_id, t.account_id, t.category_id, "
                + "c.name AS category_name, a.type AS account_type, "
                + "t.type, t.amount, t.date, t.description "
                + "FROM transactions t "
                + "JOIN categories c ON t.category_id = c.id "
                + "JOIN accounts a ON t.account_id = a.id "
                + "WHERE t.user_id = ? ORDER BY t.date DESC";
        return fetchTransactions(sql, user_id);
    }    
    
    public List<TransactionModel> getTransactionsByAccount(long account_id) {
        String sql = "SELECT t.id, t.user_id, t.account_id, t.category_id, "
                + "c.name AS category_name, a.type AS account_type, "
                + "t.type, t.amount, t.date, t.description "
                + "FROM transactions AS t "
                + "JOIN categories AS c ON t.category_id = c.id "
                + "JOIN accounts AS a ON t.account_id = a.id "
                + "WHERE t.account_id = ? ORDER BY t.date DESC";
        return fetchTransactions(sql, account_id);
    }
    
    public List<TransactionModel> getTransactionsByCategory(long category_id) {
        String sql = "SELECT t.id, t.user_id, t.account_id, t.category_id, "
                + "c.name AS category_name, a.type AS account_type, "
                + "t.type, t.amount, t.date, t.description "
                + "FROM transactions AS t "
                + "JOIN categories AS c ON t.category_id = c.id "
                + "JOIN accounts AS a ON t.account_id = a.id "
                + "WHERE t.category_id = ? ORDER BY t.date DESC";
        return fetchTransactions(sql, category_id);
    }
    
    // Helpers
    private List<TransactionModel> fetchTransactions(String sql, long id) {
        List<TransactionModel> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching transactions", e);
        }
        return transactions;
    }
    
    private TransactionModel mapRow(ResultSet rs) throws SQLException {
        TransactionModel t = new TransactionModel(
            rs.getLong("id"),
            rs.getLong("user_id"),
            AccountType.fromValue(rs.getString("account_type")),
            rs.getString("category_name"),
            TransactionType.fromValue(rs.getString("type")),
            rs.getBigDecimal("amount"),
            rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null,
            rs.getString("description")
        );
        t.setAccountId(rs.getLong("account_id"));
        t.setCategoryId(rs.getLong("category_id"));
        return t;
    }
}