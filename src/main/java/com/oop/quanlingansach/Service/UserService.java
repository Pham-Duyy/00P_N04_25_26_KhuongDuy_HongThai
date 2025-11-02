package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> authenticate(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void registerUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> updateUser(Long id, User userDetails);
    boolean changePassword(Long id, String oldPassword, String newPassword);

    // Thêm phương thức này để lấy User theo username
    Optional<User> findByUsername(String username);
}