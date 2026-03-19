/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.utils;

import com.lorraine.models.TransactionType;
import com.lorraine.services.AccountService;
import com.lorraine.services.CategoryService;
import javax.swing.JComboBox;
/**
 *
 * @author lorraineb, seany
 */
public class DataLoader {
    
    public static void loadAccounts(AccountService accountService, long user_id, JComboBox combobox) {
        combobox.removeAllItems();
        combobox.addItem("All Accounts");
        accountService.getAccountsByUser(user_id).forEach(combobox::addItem);
    }

    public static void loadCategories(CategoryService categoryService, JComboBox combobox) {
        combobox.removeAllItems();
        combobox.addItem("All Categories");
        categoryService.getCategories().forEach(combobox::addItem);
    }

    public static void loadTransactionTypes(JComboBox combobox) {
        combobox.removeAllItems();
        combobox.addItem("All Types");
        for (TransactionType type : TransactionType.values()) combobox.addItem(type);
    }
    
    // For Form
    public static void loadAccountsForForm(AccountService accountService, long userId, JComboBox combobox) {
        combobox.removeAllItems();
        accountService.getAccountsByUser(userId).forEach(combobox::addItem);
    }

    public static void loadCategoriesForForm(CategoryService categoryService, JComboBox combobox) {
        combobox.removeAllItems();
        categoryService.getCategories().forEach(combobox::addItem);
    }

    public static void loadTransactionTypesForForm(JComboBox combobox) {
        combobox.removeAllItems();
        for (TransactionType type : TransactionType.values()) combobox.addItem(type);
        combobox.setSelectedIndex(0);
    }
}