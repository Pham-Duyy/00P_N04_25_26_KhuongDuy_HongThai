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
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
long countAllUsers();
long countAllNormalUsers();
    // Thêm phương thức đếm số user theo vai trò
    long countByRole(User.Role role);
}