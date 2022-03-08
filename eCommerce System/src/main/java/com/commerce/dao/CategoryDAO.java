package com.commerce.dao;

import com.commerce.models.Category;

import java.util.ArrayList;

public interface CategoryDAO extends DAO<Category> {

    @Override
    boolean save(Category c);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<Category> getAll();
    @Override
    Category get(int id);
}
