package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.User;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private List<User> users = new ArrayList<>();

    public List<User> getAllUsers() {
        return users;
    }

    public User addUser(User user) {
        users.add(user);
        return user;
    }

    public User updateUser(Long id, User newUser) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setUsername(newUser.getUsername());
                user.setPassword(newUser.getPassword());
                user.setEmail(newUser.getEmail());
                user.setRole(newUser.getRole());
                return user;
            }
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public User findUserById(Long id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
}
