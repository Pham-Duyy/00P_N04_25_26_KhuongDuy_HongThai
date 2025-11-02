package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    // Giả lập database bằng Map
    private final Map<Long, User> userDb = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Đăng ký tài khoản mới
    public User registerUser(User user) {
        user.setId(idGenerator.getAndIncrement());
        user.setActive(true);
        user.setCreatedDate(java.time.LocalDateTime.now());
        userDb.put(user.getId(), user);
        return user;
    }

    // Xác thực đăng nhập
    public Optional<User> authenticate(String username, String password) {
        return userDb.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean existsByUsername(String username) {
        return userDb.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
    }

    // Kiểm tra email đã tồn tại chưa
    public boolean existsByEmail(String email) {
        return userDb.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    // Đổi mật khẩu
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userDb.get(userId);
        if (user != null && user.getPassword().equals(currentPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    // Lấy user theo id (trả về Optional cho phù hợp với controller)
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userDb.get(id));
    }

    // Cập nhật thông tin user
    public Optional<User> updateUser(Long userId, User userDetails) {
        User user = userDb.get(userId);
        if (user != null) {
            user.setFullName(userDetails.getFullName());
            user.setEmail(userDetails.getEmail());
            // Không cập nhật username, password, role ở đây để đảm bảo an toàn
            return Optional.of(user);
        }
        return Optional.empty();
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        return new ArrayList<>(userDb.values());
    }

    // Vô hiệu hóa tài khoản
    public boolean deactivateUser(Long userId) {
        User user = userDb.get(userId);
        if (user != null) {
            user.setActive(false);
            return true;
        }
        return false;
    }

    // Kích hoạt tài khoản
    public boolean activateUser(Long userId) {
        User user = userDb.get(userId);
        if (user != null) {
            user.setActive(true);
            return true;
        }
        return false;
    }
}