/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author lorraineb, seany
 */
public class TransactionModel {

    private long id;
    private long userID;
    private long accountID;
    private long categoryID;
    private AccountType accountType;
    private String categoryName;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

    // Constructors
    public TransactionModel(long id, long userID, AccountType accountType, String categoryName,
            TransactionType type, BigDecimal amount,
            LocalDate date, String description) {
        this.id = id;
        this.userID = userID;
        this.accountType = accountType;
        this.categoryName = categoryName;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Constructor for creating a new transaction
    public TransactionModel(long userID, long accountID, long categoryID,
            TransactionType type, BigDecimal amount,
            LocalDate date, String description) {
        this.userID = userID;
        this.accountID = accountID;
        this.categoryID = categoryID;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
    // Getters

    public long getID() {
        return id;
    }

    public long getUserID() {
        return userID;
    }

    public long getAccountID() {
        return accountID;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setAccountId(long accountID) {
        this.accountID = accountID;
    }

    public void setCategoryId(long categoryID) {
        this.categoryID = categoryID;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString
    @Override
    public String toString() {
        return "Transaction{"
                + "id=" + id
                + ", type=" + type
                + ", amount=" + amount
                + ", date=" + date
                + ", description='" + description + '\''
                + '}';
    }
}
