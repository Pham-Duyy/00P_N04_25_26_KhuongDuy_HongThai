package com.fund.group09.Service;

import com.fund.group09.Model.User;
import com.fund.group09.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //  Lấy tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //  Lấy user theo id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    //  Kiểm tra email tồn tại
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //  Đăng ký user mới
    public User registerUser(User user) {
        // Hash password trước khi lưu (có thể dùng BCrypt)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    //  Xác thực đăng nhập
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    //  Cập nhật thông tin user
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        });
    }

    //  Xóa user
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }

    //  Đổi mật khẩu
    public boolean changePassword(Long id, String oldPass, String newPass) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (!user.getPassword().equals(oldPass)) {
            return false;
        }

        user.setPassword(newPass);
        userRepository.save(user);
        return true;
    }
}
