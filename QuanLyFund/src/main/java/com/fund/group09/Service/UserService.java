package com.fund.group09.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fund.group09.Model.User;
import com.fund.group09.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Dùng BCrypt để mã hóa và kiểm tra mật khẩu
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Đăng ký người dùng mới
    public void register(User user) throws Exception {
        // Kiểm tra trùng username
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }

        // Kiểm tra trùng email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email đã được sử dụng!");
        }

        // Mã hóa mật khẩu trước khi lưu
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
    }

    // Đăng nhập bằng email và kiểm tra mật khẩu đã mã hóa
    public User loginByEmail(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // So sánh mật khẩu đã mã hóa
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
