/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lorraine.views;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.lorraine.main.Session;
import com.lorraine.models.AccountModel;
import com.lorraine.models.CategoryModel;
import com.lorraine.models.TransactionModel;
import com.lorraine.models.TransactionType;
import com.lorraine.services.AccountService;
import com.lorraine.services.CategoryService;
import com.lorraine.services.TransactionService;
import com.lorraine.swing.CheckBoxTableHeaderRenderer;
import com.lorraine.swing.TableHeaderAlignment;
import com.lorraine.utils.CurrencyFormatter;
import com.lorraine.utils.DataLoader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import raven.datetime.DatePicker;
import raven.popup.DefaultOption;
import raven.popup.GlassPanePopup;
import raven.popup.component.PopupController;
import raven.popup.component.SimplePopupBorder;
import raven.toast.Notifications;

/**
 *
 * @author Lorraine G. Bulaclac, seany
 */
public class TransactionsPanel extends javax.swing.JPanel {

    private static final Logger LOGGER = Logger.getLogger(TransactionsPanel.class.getName());
    
    private DatePicker datePicker;
    private final List<TransactionModel> loadedTransactions = new ArrayList<>();

    private final TransactionService transactionService = new TransactionService();
    private final CategoryService categoryService = new CategoryService();
    private final AccountService accountService = new AccountService();

    /**
     * Creates new form TransactionsPanel
     */
    public TransactionsPanel() {
        initComponents();
        initDatePicker();
        initComponentProperties();
        initSearchControls();
        loadTransactions();
    }

    private void initComponentProperties() {
        String roundedStyle = "arc:15; borderWidth:1; focusWidth:0; innerFocusWidth:0;";

        typesFilterCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        datePickerField.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        accountsFilterCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);
        categoriesFilterCBX.putClientProperty(FlatClientProperties.STYLE, roundedStyle);

        tableContainer.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:25;"
                + "background:$Table.background");

        transactionTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;"
                + "font:bold;");

        transactionTable.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");

        tableScroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:999;"
                + "trackInsets:3,3,3,3;"
                + "thumbInsets:3,3,3,3;"
                + "background:$Table.background;");

        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search Description");
        searchField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("icons/search.svg"));
        searchField.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:15;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;");

        transactionTable.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(transactionTable, 0));
        transactionTable.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(transactionTable));
    }

    private void initDatePicker() {
        datePicker = new DatePicker();
        datePicker.setEditor(datePickerField);
        datePicker.setCloseAfterSelected(true);
        datePicker.setDateSelectionAble(date -> !date.isAfter(LocalDate.now()));
    }

    private void initSearchControls() {
        DataLoader.loadAccounts(accountService, Session.getUserID(), accountsFilterCBX);
        DataLoader.loadCategories(categoryService, categoriesFilterCBX);
        DataLoader.loadTransactionTypes(typesFilterCBX);

        DocumentListener onType = new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        };

        searchField.getDocument().addDocumentListener(onType);
        accountsFilterCBX.addActionListener(e -> performSearch());
        categoriesFilterCBX.addActionListener(e -> performSearch());
        typesFilterCBX.addActionListener(e -> performSearch());
        datePicker.addDateSelectionListener(e -> performSearch());
    }

    private void performSearch() {
        String description = searchField.getText().trim();
        String accountName = accountsFilterCBX.getSelectedIndex() == 0 ? null : ((AccountModel) accountsFilterCBX.getSelectedItem()).getType().getValue();
        String categoryName = categoriesFilterCBX.getSelectedIndex() == 0 ? null : ((CategoryModel) categoriesFilterCBX.getSelectedItem()).getName();
        TransactionType type = typesFilterCBX.getSelectedIndex() == 0 ? null : (TransactionType) typesFilterCBX.getSelectedItem();
        LocalDate date = datePicker.isDateSelected() ? datePicker.getSelectedDate() : null;

        populateTable(transactionService.searchTransactions(
                Session.getUserID(), description,
                accountName,
                categoryName,
                type, date
        ));
    }

    private void loadTransactions() {
        populateTable(transactionService.getTransactionsByUser(Session.getUserID()));
    }

    private void populateTable(List<TransactionModel> transactions) {
        try {
            DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
            if (transactionTable.isEditing()) {
                transactionTable.getCellEditor().stopCellEditing();
            }
            model.setRowCount(0);
            loadedTransactions.clear();
            for (TransactionModel t : transactions) {
                model.addRow(new Object[]{
                    false,
                    t.getDate(),
                    t.getDescription(),
                    t.getCategoryName(),
                    t.getAccountType().getDisplayValue(),
                    t.getType().getDisplayValue(),
                    CurrencyFormatter.format(t.getAmount())
                }); 
                loadedTransactions.add(t);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error populating table", e);
        }
    }

    private List<TransactionModel> getSelectedData() {
        List<TransactionModel> selected = new ArrayList<>();
        for (int i = 0; i < transactionTable.getRowCount(); i++) {
            boolean isChecked = (boolean) transactionTable.getValueAt(i, 0);
            if (isChecked) {
                selected.add(loadedTransactions.get(i));
            }
        }
        return selected;
    }
    
    private void showTransactionPopup(String title, String confirmLabel,
                                       TransactionForm form,
                                       TransactionPopupAction onConfirm) {
        DefaultOption option = new DefaultOption() {
            @Override public boolean closeWhenClickOutside() { return true; }
        };

        GlassPanePopup.showPopup(new SimplePopupBorder(form, title,
            new String[]{confirmLabel, "Cancel"}, (pc, i) -> {
                if (i != 0) { pc.closePopup(); return; }
                
                TransactionModel data = form.getData();
                if (data == null) {
                    Notifications.getInstance().show(Notifications.Type.WARNING, "Please fill in all fields.");
                    return;
                }
                
                onConfirm.execute(pc, data);
            }), option);
    }

    @FunctionalInterface
    private interface TransactionPopupAction {
        void execute(PopupController pc, TransactionModel data);
    }

    private TransactionForm buildForm() {
        TransactionForm form = new TransactionForm();
        DataLoader.loadAccountsForForm(accountService, Session.getUserID(), form.getAccountCBX());
        DataLoader.loadCategoriesForForm(categoryService, form.getCategoryCBX());
        DataLoader.loadTransactionTypesForForm(form.getTypeCBX());
        return form;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox3 = new javax.swing.JComboBox<>();
        mainContainer = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        accountsFilterCBX = new javax.swing.JComboBox<>();
        categoriesFilterCBX = new javax.swing.JComboBox<>();
        typesFilterCBX = new javax.swing.JComboBox<>();
        datePickerField = new javax.swing.JFormattedTextField();
        tableContainer = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        mainContainer.setPreferredSize(new java.awt.Dimension(820, 620));

        headerPanel.setPreferredSize(new java.awt.Dimension(772, 36));

        titleLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        titleLabel.setText("Transactions");

        addButton.setBackground(new java.awt.Color(19, 210, 27));
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        addButton.setText("Add Transaction");
        addButton.setBorder(null);
        addButton.setFocusPainted(false);
        addButton.addActionListener(this::addButtonActionPerformed);

        editButton.setText("Edit");
        editButton.setBorder(null);
        editButton.setFocusPainted(false);
        editButton.addActionListener(this::editButtonActionPerformed);

        deleteButton.setBackground(new java.awt.Color(220, 26, 55));
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.setBorder(null);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(this::deleteButtonActionPerformed);

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        filterPanel.setPreferredSize(new java.awt.Dimension(773, 36));

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(accountsFilterCBX, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(categoriesFilterCBX, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(typesFilterCBX, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(datePickerField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datePickerField)
                    .addComponent(searchField, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(typesFilterCBX)
                    .addComponent(categoriesFilterCBX)
                    .addGroup(filterPanelLayout.createSequentialGroup()
                        .addComponent(accountsFilterCBX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tableContainer.setPreferredSize(new java.awt.Dimension(815, 468));

        tableScroll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SELECT", "DATE", "DESCRIPTION", "CATEGORY", "ACCOUNT", "TYPE", "AMOUNT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transactionTable.setPreferredSize(new java.awt.Dimension(760, 300));
        transactionTable.setRequestFocusEnabled(false);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        tableScroll.setViewportView(transactionTable);
        if (transactionTable.getColumnModel().getColumnCount() > 0) {
            transactionTable.getColumnModel().getColumn(0).setMaxWidth(50);
            transactionTable.getColumnModel().getColumn(1).setPreferredWidth(120);
            transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            transactionTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            transactionTable.getColumnModel().getColumn(5).setPreferredWidth(80);
            transactionTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        javax.swing.GroupLayout tableContainerLayout = new javax.swing.GroupLayout(tableContainer);
        tableContainer.setLayout(tableContainerLayout);
        tableContainerLayout.setHorizontalGroup(
            tableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
        );
        tableContainerLayout.setVerticalGroup(
            tableContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(426, 445, Short.MAX_VALUE))
            .addGroup(tableContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScroll)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainContainerLayout = new javax.swing.GroupLayout(mainContainer);
        mainContainer.setLayout(mainContainerLayout);
        mainContainerLayout.setHorizontalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
                    .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );
        mainContainerLayout.setVerticalGroup(
            mainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainContainerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tableContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        TransactionForm form = buildForm();
        showTransactionPopup("Add Transaction", "Add Transaction", form, (pc, data) -> {
            boolean success = transactionService.addTransaction(
                    Session.getUserID(), data.getAccountID(), data.getCategoryID(),
                    data.getType(), data.getAmount(), data.getDate(), data.getDescription());
            if (success) {
                pc.closePopup();
                loadTransactions();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Transaction added successfully.");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to add transaction.");
            }
        });
    }//GEN-LAST:event_addButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        List<TransactionModel> selected = getSelectedData();
        
        if (selected.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select a transaction to edit.");
            return;
        }
        if (selected.size() > 1) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select only one transaction to edit.");
            return;
        }

        TransactionModel existing = selected.get(0);
        TransactionForm form = buildForm();
        form.prefill(existing);
        
        showTransactionPopup("Edit Transaction", "Save Changes", form, (pc, data) -> {
            boolean success = transactionService.editTransaction(
                existing.getID(), data.getAccountID(), data.getCategoryID(),
                data.getType(), data.getAmount(), data.getDate(), data.getDescription()
            );
            if (success) {
                pc.closePopup();
                loadTransactions();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Transaction updated successfully.");
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Failed to update transaction.");
            }
        });
    }//GEN-LAST:event_editButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        List<TransactionModel> selected = getSelectedData();
        
        if (selected.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Please select a transaction to delete.");
            return;
        }
        DefaultOption option = new DefaultOption() {
            @Override public boolean closeWhenClickOutside() { return true; }
        };

        String message = "Are you sure you want to delete " + selected.size() + " transaction(s)?";
        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GlassPanePopup.showPopup(new SimplePopupBorder(label, "Confirm Delete",
            new String[]{"Delete", "Cancel"}, (pc, i) -> {
                if (i != 0) { pc.closePopup(); return; }
                selected.forEach(t -> transactionService.deleteTransaction(t.getID()));
                pc.closePopup();
                loadTransactions();
                Notifications.getInstance().show(Notifications.Type.SUCCESS,
                    selected.size() + " transaction(s) deleted.");
            }), option);
    }//GEN-LAST:event_deleteButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> accountsFilterCBX;
    private javax.swing.JButton addButton;
    private javax.swing.JComboBox<String> categoriesFilterCBX;
    private javax.swing.JFormattedTextField datePickerField;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel tableContainer;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTable transactionTable;
    private javax.swing.JComboBox<String> typesFilterCBX;
    // End of variables declaration//GEN-END:variables
}
