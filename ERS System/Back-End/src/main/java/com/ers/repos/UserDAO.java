package com.ers.repos;

import com.ers.models.User;

import java.util.List;

public interface UserDAO extends DAO<User> {

    User getUserByUsername(String username);

    @Override
    boolean save(User o);
    @Override
    boolean update(User o);
    @Override
    boolean delete(int id);
    @Override
    List<User> getAll();
    @Override
    User get(int id);
}
