package com.commerce.dao;

import com.commerce.models.Product;

import java.util.ArrayList;

public interface ProductDAO extends DAO<Product> {

    @Override
    boolean save(Product p);
    @Override
    boolean update(Product p);
    @Override
    boolean delete(int id);
    @Override
    ArrayList<Product> getAll();
    @Override
    Product get(int id);
}
