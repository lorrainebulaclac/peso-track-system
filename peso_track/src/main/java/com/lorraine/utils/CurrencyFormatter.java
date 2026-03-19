/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
/**
 *
 * @author lorraineb, seany
 */
public class CurrencyFormatter {
    private static final Locale LOCALE_PH = Locale.forLanguageTag("en-PH");
    
    private static NumberFormat getCurrencyFormat() {
        return NumberFormat.getCurrencyInstance(LOCALE_PH);
    }
    
    public static void setup(JFormattedTextField field) {
        NumberFormatter formatter = new NumberFormatter(getCurrencyFormat());
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(false);

        field.setFormatterFactory(new DefaultFormatterFactory(formatter));
        field.setValue(BigDecimal.ZERO);
    }
    
    // ─── Read Value ───────────────────────────────────────────
    
    public static BigDecimal getValue(JFormattedTextField field) {
        Number value = (Number) field.getValue();
        if (value == null) return BigDecimal.ZERO;
        return new BigDecimal(value.toString());
    }
    
    // ─── Display Formatting ───────────────────────────────────
    
    public static String format(BigDecimal amount) {
        if (amount == null) return getCurrencyFormat().format(0);
        return getCurrencyFormat().format(amount);
    }
}
