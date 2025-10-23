package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.User;

@RestController
@RequestMapping("/users")
public class UserController {
    private List<User> users = new ArrayList<>();

    // Lấy tất cả user
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    // Thêm mới user
    @PostMapping
    public User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    // Sửa user theo id
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User newUser) {
        User user = findUserById(id);
        if (user != null) {
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());
            user.setRole(newUser.getRole());
            return user;
        }
        return null;
    }

    // Xóa user theo id
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = findUserById(id);
        if (user != null) {
            users.remove(user);
            return "Xóa thành công";
        }
        return "Không tìm thấy user";
    }

    // Hàm tiện ích tìm user theo id
    private User findUserById(Long id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
}
