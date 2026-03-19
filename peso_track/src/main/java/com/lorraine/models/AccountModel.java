/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

import java.math.BigDecimal;

/**
 *
 * @author lorraineb, seany
 */
public class AccountModel {

    private final long id;
    private final long userID;
    private final AccountType type;
    private final BigDecimal balance;

    public AccountModel(long id, long userID, AccountType type, BigDecimal balance) {
        this.id = id;
        this.userID = userID;
        this.type = type;
        this.balance = balance;
    }

    // Getters
    public long getID() {
        return id;
    }

    public long getUserID() {
        return userID;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return type.getDisplayValue();
    }
}
