package com.ers.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserDAOImplTest {

    public UserDAO testInstance;

    @BeforeEach
    void setUp(){
        testInstance = new UserDAOImpl();
    }

    @Test
    void getUserByUsername() {
        assertNotNull(testInstance.getUserByUsername("marioj"));
    }
}
