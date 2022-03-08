package com.commerce.services;

import com.commerce.dao.CategoryDAO;
import com.commerce.dao.CategoryDAOImpl;
import com.commerce.models.Category;

import java.util.ArrayList;

public class CategoryService {

    private CategoryDAO categoryDAO = new CategoryDAOImpl();

    public ResponseType createCategory(Category c){
        // Checks if category fields are valid
        if (c.getName().isEmpty() || c.getDescription().isEmpty()) {
            return ResponseType.INVALID_FIELDS;
        }
        // Tries to save category
        if (categoryDAO.save(c)){
            return ResponseType.SUCCESS;
        }
        return ResponseType.UNKNOWN_ERROR;
    }

    public Category getCategoryById(int id) { return categoryDAO.get(id); }

    public ArrayList<Category> getAllCategories() { return categoryDAO.getAll();}

}
