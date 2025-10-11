package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.User;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private List<User> users = new ArrayList<>();

    public List<User> findAll() {
        return users;
    }

    public void save(User user) {
        users.add(user);
    }

    public User findById(Long id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public boolean update(Long id, User newUser) {
        User user = findById(id);
        if (user != null) {
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            user.setRole(newUser.getRole());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        User user = findById(id);
        if (user != null) {
            users.remove(user);
            return true;
        }
        return false;
    }
}
