/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

/**
 *
 * @author lorraineb, seany
 */
public class ValidationResult {
    
    private final boolean valid;
    private final String errorMessage;
    
    private ValidationResult(boolean valid, String errorMessage){
        this.valid = valid;
        this.errorMessage = errorMessage;
    }
    
    public boolean isValid(){
        return valid;
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public static ValidationResult success(){
        return new ValidationResult(true, "");
    }
    
    public static ValidationResult failed(String errorMessage){
        return new ValidationResult(false, errorMessage);
    }
}
