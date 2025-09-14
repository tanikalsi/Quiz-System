// Place this in the com.quizsystem.dao package
package com.quizsystem.dao;

import com.quizsystem.model.User;

public interface UserDao {
    void save(User user);
    User findByUsername(String username);
}