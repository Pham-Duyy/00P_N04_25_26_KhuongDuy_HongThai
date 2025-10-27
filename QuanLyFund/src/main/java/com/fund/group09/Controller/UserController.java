package com.fund.group09.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fund.group09.Model.User;
import com.fund.group09.Service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    // ✅ Đăng ký tài khoản mới
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody User user) {
        // Kiểm tra độ mạnh của mật khẩu
        if (!isStrongPassword(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Mật khẩu phải từ 8 ký tự trở lên, gồm chữ hoa, chữ thường, số và ký tự đặc biệt"));
        }

        // Gán vai trò mặc định nếu chưa có
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }

        // Gọi service để lưu người dùng
        try {
            userService.register(user);
            return ResponseEntity.ok(Map.of("message", "Đăng ký thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Đăng nhập bằng email và role
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        String role = loginRequest.get("role"); // Lấy role từ frontend

        User loggedInUser = userService.loginByEmail(email, password);
        if (loggedInUser != null) {
            // Kiểm tra role hợp lệ
            if (role != null && !role.equalsIgnoreCase(loggedInUser.getRole())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Bạn không có quyền đăng nhập với vai trò này"));
            }
            // Lưu thông tin vào session
            session.setAttribute("userId", loggedInUser.getId());
            session.setAttribute("userEmail", loggedInUser.getEmail());
            session.setAttribute("userRole", loggedInUser.getRole());
            session.setAttribute("userName", loggedInUser.getName());
            session.setAttribute("isLoggedIn", true);

            // Trả về thông tin user cho frontend
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", loggedInUser.getId());
            userMap.put("email", loggedInUser.getEmail());
            userMap.put("name", loggedInUser.getName());
            userMap.put("role", loggedInUser.getRole());

            return ResponseEntity.ok(Map.of("user", userMap));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Sai email hoặc mật khẩu"));
        }
    }

    // ✅ Đăng xuất
    @GetMapping("/logout")
public ResponseEntity<?> logout() {
    session.invalidate(); // Xóa toàn bộ session
    return ResponseEntity.ok(Map.of("message", "Đã đăng xuất"));
}

    // ✅ Kiểm tra độ mạnh của mật khẩu
    private boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[^A-Za-z0-9]").matcher(password).find();

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    // ✅ Lấy thông tin user hiện tại (nếu cần)
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Object userId = session.getAttribute("userId");
        Object userEmail = session.getAttribute("userEmail");
        Object userRole = session.getAttribute("userRole");
        Object userName = session.getAttribute("userName");

        if (userId == null || userEmail == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Chưa đăng nhập"));
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", userId);
        userMap.put("email", userEmail);
        userMap.put("role", userRole);
        userMap.put("name", userName);

        return ResponseEntity.ok(Map.of("user", userMap));
    }
}