/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.models;

import java.time.LocalDate;

/**
 *
 * @author lorraineb, seany
 */
public class UserModel {
    private long user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private LocalDate created_at;
    
    // Constructors
    
    public UserModel(long user_id, String first_name, String last_name, String email, String password, LocalDate created_at) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
    }
    
    public UserModel(String first_name, String last_name, String email, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email     = email;
        this.password  = password;
    }
    
    // Getters
    
    public long getUserID()         { return user_id; }
    public String getFullName()     { return first_name + " " + last_name; }
    public String getFirstName()    { return first_name; }
    public String getLastName()     { return last_name; }
    public String getEmail()        { return email.toLowerCase(); }
    public String getPassword()     { return password; }
    public LocalDate getCreatedAt() { return created_at; }
    
    // Setters
    
    public void setFirstName(String first_name) { this.first_name = first_name; }
    public void setLastName(String last_name)   { this.last_name = last_name; }
    public void setEmail(String email)          { this.email = email; }
    public void setPassword(String password)    {  this.password = password; }
    
    // toString
    
    @Override
    public String toString() {
        return "User{" +
                "user_id="     + user_id +
                ", fullName='" + getFullName() + '\'' +
                ", email='"   + email + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
