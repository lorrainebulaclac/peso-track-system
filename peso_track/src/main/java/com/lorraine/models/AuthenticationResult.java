/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

/**
 *
 * @author lorraineb, seany
 */
public class AuthenticationResult {
    private final boolean success;
    private final String message;
    
    private AuthenticationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public static AuthenticationResult success(String message) {
        return new AuthenticationResult(true, message);
    }
    
    public static AuthenticationResult failed(String message) {
        return new AuthenticationResult(false, message);
    }
}
