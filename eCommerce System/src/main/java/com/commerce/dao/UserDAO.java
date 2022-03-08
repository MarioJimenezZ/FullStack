package com.commerce.dao;

import com.commerce.models.User;
import java.util.ArrayList;


public interface UserDAO extends DAO<User> {

    User getByEmail(String email);

    @Override
    ArrayList<User> getAll();
    @Override
    User get(int id);
    @Override
    boolean save(User a);
    @Override
    boolean update(User o);
    @Override
    boolean delete(int id);

}
