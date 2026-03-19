/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lorraine.views;

import com.formdev.flatlaf.FlatClientProperties;
import com.lorraine.main.Session;
import com.lorraine.models.AccountModel;
import com.lorraine.models.CategoryModel;
import com.lorraine.models.TransactionModel;
import com.lorraine.models.TransactionType;
import com.lorraine.utils.CurrencyFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;
import raven.datetime.DatePicker;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author lorraineb, seany
 */
public class TransactionForm extends javax.swing.JPanel {

    
    private static final Logger LOGGER = Logger.getLogger(TransactionForm.class.getName());
    private DatePicker datePicker;
    
    /**
     * Creates new form addTransactionData
     */
    public TransactionForm() {
        init();
    }
    
    private void init() {
        initComponents();
        initDatePicker();
        initComponentProperties();
        CurrencyFormatter.setup(amountField);
    }
    
    private void initDatePicker() {
        datePicker = new DatePicker();
        datePicker.setEditor(datePickerField);
        datePicker.setCloseAfterSelected(true);
        datePicker.setDateSelectionAble(date -> !date.isAfter(LocalDate.now()));
        datePicker.setSelectedDate(LocalDate.now());
    }

    private void initComponentProperties() {
        String roundedStyle = "arc:15;borderWidth:0;focusWidth:0;innerFocusWidth:0;";
        
        typeCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        datePickerField.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        accountCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        categoryCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        descriptionField.putClientProperty(FlatClientProperties.STYLE, roundedStyle + "margin:5,10,5,10;");
        
        descriptionField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. Jollibee Lunch, Netflix Subscription...");
        amountField.putClientProperty(FlatClientProperties.STYLE, roundedStyle + "margin:5,10,5,10;");
    }
    
    public void prefill(TransactionModel t) {
        if (t == null) return;
        
        typeCBX.setSelectedItem(t.getType());
        descriptionField.setText(t.getDescription());
        amountField.setValue(t.getAmount());
        datePicker.setSelectedDate(t.getDate());
        
        // Account — match by id
        for (int i = 0; i < accountCBX.getItemCount(); i++) {
            AccountModel a = (AccountModel) accountCBX.getItemAt(i);
            if (a.getID() == t.getAccountID()) {
                accountCBX.setSelectedItem(a);
                break;
            }
        }

        // Category — match by id
        for (int i = 0; i < categoryCBX.getItemCount(); i++) {
            CategoryModel c = (CategoryModel) categoryCBX.getItemAt(i);
            if (c.getID() == t.getCategoryID()) {
                categoryCBX.setSelectedItem(c);
                break;
            }
        }
    }
    
    public TransactionModel getData() {
        
        try {
            amountField.commitEdit();
        } catch (java.text.ParseException e) {
            LOGGER.log(Level.WARNING, "Invalid amount input", e);
        }
        
        if (!isFormValid()) return null;
        
        TransactionType type    = (TransactionType) typeCBX.getSelectedItem();
        LocalDate date          = datePicker.isDateSelected() ? datePicker.getSelectedDate() : LocalDate.now();
        String description      = descriptionField.getText().trim();
        BigDecimal amount       = CurrencyFormatter.getValue(amountField);
        AccountModel account    = (AccountModel) accountCBX.getSelectedItem();
        CategoryModel category  = (CategoryModel) categoryCBX.getSelectedItem();

        return new TransactionModel(Session.getUserID(), account.getID(), category.getID(), type, amount, date, description);
    }
    
    private boolean isFormValid() {
        return typeCBX.getSelectedItem() != null
            && accountCBX.getSelectedItem() != null
            && categoryCBX.getSelectedItem() != null
            && datePicker.getSelectedDate() != null
            && !descriptionField.getText().trim().isEmpty()
            && CurrencyFormatter.getValue(amountField).compareTo(BigDecimal.ZERO) > 0;
    }
    
    public JComboBox<TransactionType> getTypeCBX()      { return typeCBX; }
    public JComboBox<AccountModel> getAccountCBX()       { return accountCBX; }
    public JComboBox<CategoryModel> getCategoryCBX()    { return categoryCBX; }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TransactionFormContainer = new javax.swing.JPanel();
        descriptionField = new javax.swing.JTextField();
        dateLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        categoryLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        amountLabel = new javax.swing.JLabel();
        datePickerField = new javax.swing.JFormattedTextField();
        accountLabel = new javax.swing.JLabel();
        categoryCBX = new javax.swing.JComboBox<>();
        typeCBX = new javax.swing.JComboBox<>();
        accountCBX = new javax.swing.JComboBox<>();
        amountField = new javax.swing.JFormattedTextField();

        descriptionField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        dateLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        dateLabel.setText("TRANSACTION DATE");

        descriptionLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        descriptionLabel.setText("DESCRIPTION");

        categoryLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        categoryLabel.setText("CATEGORY");

        typeLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        typeLabel.setText("TRANSACTION TYPE");

        amountLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        amountLabel.setText("AMOUNT (₱)");

        datePickerField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        accountLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        accountLabel.setText("ACCOUNT");

        categoryCBX.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        typeCBX.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        accountCBX.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout TransactionFormContainerLayout = new javax.swing.GroupLayout(TransactionFormContainer);
        TransactionFormContainer.setLayout(TransactionFormContainerLayout);
        TransactionFormContainerLayout.setHorizontalGroup(
            TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransactionFormContainerLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(accountCBX, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, TransactionFormContainerLayout.createSequentialGroup()
                        .addGroup(TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(typeCBX, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datePickerField)
                            .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(descriptionField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoryCBX, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(amountLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(amountField))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        TransactionFormContainerLayout.setVerticalGroup(
            TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TransactionFormContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(TransactionFormContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(typeCBX)
                    .addComponent(datePickerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(amountLabel)
                .addGap(0, 0, 0)
                .addComponent(amountField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(accountCBX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(categoryCBX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TransactionFormContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TransactionFormContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel TransactionFormContainer;
    private javax.swing.JComboBox<AccountModel> accountCBX;
    private javax.swing.JLabel accountLabel;
    private javax.swing.JFormattedTextField amountField;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JComboBox<CategoryModel> categoryCBX;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JFormattedTextField datePickerField;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JComboBox<TransactionType> typeCBX;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}