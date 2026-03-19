/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.main;

import com.lorraine.models.UserModel;
/**
 *
 * @author lorraineb, seany
 */
public class Session {
    
    private  static UserModel currentUser;
    
    public Session() {}
    
    public static void login(UserModel user) {
        currentUser = user;
    }
    
    public static void logout() {
        currentUser = null;
    }
    
    public static UserModel getUser() {
        return currentUser;
    }
    
    public static long getUserID() {
        if (currentUser == null) throw new IllegalStateException("No user logged in.");
        return currentUser.getUserID();
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
