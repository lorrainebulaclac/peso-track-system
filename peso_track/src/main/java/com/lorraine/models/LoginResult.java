/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

/**
 *
 * @author lorraineb, seany
 */
public class LoginResult {
    private final boolean success;
    private final String message;
    private final UserModel user;
    
    private LoginResult(boolean success, String message, UserModel user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public UserModel getUser() {
        return user;
    }
    
    public static LoginResult success(String message, UserModel user) {
        return new LoginResult(true, message, user);
    }
    
    public static LoginResult failed(String message) {
        return new LoginResult(false, message, null);
    }
}
