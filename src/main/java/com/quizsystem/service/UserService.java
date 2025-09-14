// Place this in the com.quizsystem.service package
package com.quizsystem.service;

import com.quizsystem.dao.UserDao;
import com.quizsystem.model.User;

public class UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void createUser(String username, String role) {
        if (userDao.findByUsername(username) != null) {
            System.out.println("User '" + username + "' already exists.");
            return;
        }
        User newUser = new User(username, role);
        userDao.save(newUser);
        System.out.println("SUCCESS: Created user -> " + newUser);
    }
}