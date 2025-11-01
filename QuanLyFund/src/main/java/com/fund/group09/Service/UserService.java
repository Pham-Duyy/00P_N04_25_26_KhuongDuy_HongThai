package com.fund.group09.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fund.group09.Model.User;
import com.fund.group09.Repository.UserRepository;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Đăng ký người dùng mới
    public void register(User user) throws Exception {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email đã được sử dụng!");
        }
        userRepository.save(user);
    }

    // Đăng nhập bằng email và kiểm tra mật khẩu thông thường
    public User loginByEmail(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (password.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    // Lấy tất cả user
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Tìm user theo email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email: " + email));
    }

    // Tìm user theo id (bổ sung để dùng cho TransactionController)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với id: " + id));
    }
}