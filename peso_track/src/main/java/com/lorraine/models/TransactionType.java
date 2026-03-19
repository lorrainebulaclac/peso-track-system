/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.lorraine.models;

/**
 *
 * @author lorraineb, seany
 */
public enum TransactionType {
    INCOME("income"),
    EXPENSE("expense"),
    TRANSFER("transfer");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayValue() {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    @Override
    public String toString() {
        return getDisplayValue();
    }

    public static TransactionType fromValue(String value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + value);
    }
}
