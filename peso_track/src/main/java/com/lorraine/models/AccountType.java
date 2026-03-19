/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.lorraine.models;

/**
 *
 * @author lorraineb, seany
 */
public enum AccountType {
    CASH("Cash"),
    SAVINGS("Savings"),
    CREDIT_CARD("Credit Card"),
    OTHER("Other");
    
    private final String value;
    
    AccountType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDisplayValue() {
        return value.contains(" ") ? value.substring(0, 1).toUpperCase() + value.substring(1) : value.substring(0, 1).toUpperCase() + value.substring(1);
    }
    
    public static AccountType fromValue(String value) {
        for (AccountType type : AccountType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + value);
    }
}