/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lorraine.services;

import com.lorraine.config.DatabaseConnection;
import com.lorraine.models.CategoryModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorraineb, seany
 */
public class CategoryService {

    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    // Get Categories
    public List<CategoryModel> getCategories() {
        String sql = "SELECT id, name " +
                     "FROM categories " +
                     "ORDER BY name ASC";

        List<CategoryModel> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
                while (rs.next()) {
                    categories.add(mapCategory(rs));
                }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories" + e.getMessage(), e);
        }
        return categories;
    }

    private CategoryModel mapCategory(ResultSet rs) throws SQLException {
        return new CategoryModel(
            rs.getLong("id"),
            rs.getString("name")
        );
    }
}