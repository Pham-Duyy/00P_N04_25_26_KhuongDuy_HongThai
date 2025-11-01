package com.fund.group09.Controller;

import com.fund.group09.Model.User;
import com.fund.group09.Repository.UserRepository;
import com.fund.group09.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            HttpSession session,
            Model model) {
        // Nếu đã đăng nhập, redirect về dashboard tương ứng
        if (isLoggedIn(session)) {
            String userRole = (String) session.getAttribute("userRole");
            if ("ADMIN".equals(userRole)) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equals(userRole)) {
                return "redirect:/user/dashboard";
            }
        }
        if (error != null) {
            model.addAttribute("error", "Thông tin đăng nhập không chính xác!");
        }
        if (logout != null) {
            model.addAttribute("message", "Đăng xuất thành công!");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            HttpSession session,
            Model model) {
        
        System.out.println("🔍 Login attempt - Email: " + email + ", Role: " + role);
        
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "Email không được để trống!");
            return "auth/login";
        }
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Mật khẩu không được để trống!");
            return "auth/login";
        }

        try {
            // Tìm user trong database
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                System.out.println("❌ User not found: " + email);
                model.addAttribute("error", "Email không tồn tại!");
                return "auth/login";
            }

            User user = optionalUser.get();
            System.out.println("✅ Found user: " + user.getEmail() + " with role: " + user.getRole());
            
            // Debug password comparison
            System.out.println("🔍 Password comparison:");
            System.out.println("   Input password: '" + password + "' (length: " + password.length() + ")");
            System.out.println("   Stored password: '" + user.getPassword() + "' (length: " + (user.getPassword() != null ? user.getPassword().length() : 0) + ")");
            System.out.println("   Passwords match: " + password.equals(user.getPassword()));

            // So sánh mật khẩu trực tiếp (trim để loại bỏ khoảng trắng thừa)
            String inputPassword = password.trim();
            String storedPassword = user.getPassword() != null ? user.getPassword().trim() : "";
            
            if (!inputPassword.equals(storedPassword)) {
                System.out.println("❌ Wrong password for user: " + email);
                model.addAttribute("error", "Mật khẩu không đúng!");
                return "auth/login";
            }

            // Kiểm tra vai trò
            if (!role.equalsIgnoreCase(user.getRole())) {
                System.out.println("❌ Wrong role for user: " + email + " (expected: " + user.getRole() + ", got: " + role + ")");
                model.addAttribute("error", "Vai trò không đúng!");
                return "auth/login";
            }

            System.out.println("✅ Login successful for user: " + email + " with role: " + user.getRole());

            // Lưu thông tin vào session
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());

            // Redirect dựa trên role
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                System.out.println("🔄 Redirecting to admin dashboard");
                return "redirect:/admin/dashboard";
            } else if ("USER".equalsIgnoreCase(user.getRole())) {
                System.out.println("🔄 Redirecting to user dashboard");
                return "redirect:/user/dashboard";
            }

            model.addAttribute("error", "Vai trò không hợp lệ!");
            return "auth/login";

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Đăng nhập thất bại: " + e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "success", required = false) String success,
            HttpSession session,
            Model model) {
        // Nếu đã đăng nhập, redirect về dashboard
        if (isLoggedIn(session)) {
            String userRole = (String) session.getAttribute("userRole");
            if ("ADMIN".equals(userRole)) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equals(userRole)) {
                return "redirect:/user/dashboard";
            }
        }
        if (success != null) {
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        }
        return "auth/register";
    }

    // ...existing code...
@PostMapping("/register")
public String doRegister(@RequestParam("name") String name,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("confirmPassword") String confirmPassword,
        @RequestParam("role") String role,
        Model model) {

    System.out.println("🔍 Register attempt - Email: " + email + ", Role: " + role);

    // Validate input
    if (name == null || name.trim().isEmpty()) {
        model.addAttribute("error", "Tên không được để trống!");
        return "auth/register";
    }
    if (email == null || email.trim().isEmpty()) {
        model.addAttribute("error", "Email không được để trống!");
        return "auth/register";
    }
    if (password == null || password.trim().isEmpty()) {
        model.addAttribute("error", "Mật khẩu không được để trống!");
        return "auth/register";
    }
    if (!password.equals(confirmPassword)) {
        model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
        return "auth/register";
    }
    if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
        model.addAttribute("error", "Vai trò không hợp lệ!");
        return "auth/register";
    }

    try {
        // Kiểm tra email đã tồn tại
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email này đã được đăng ký!");
            return "auth/register";
        }

        // Tạo user mới
        User newUser = new User();
        newUser.setName(name.trim());
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setPassword(password.trim()); // Lưu mật khẩu trực tiếp, không mã hóa
        newUser.setRole(role.toUpperCase());
        // Sửa tại đây: set username mặc định là email (hoặc name tuỳ ý)
        newUser.setUsername(email.trim().toLowerCase());

        // Lưu user trực tiếp vào database
        User savedUser = userRepository.save(newUser);

        System.out.println("✅ Registration successful for: " + email);
        System.out.println("   Saved user ID: " + savedUser.getId());
        System.out.println("   Saved password: '" + savedUser.getPassword() + "'");

        return "redirect:/register?success";

    } catch (Exception e) {
        System.err.println("❌ Registration error: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
        return "auth/register";
    }
}
// ...existing code...

    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        String userRole = (String) session.getAttribute("userRole");
        if ("ADMIN".equals(userRole)) {
            return "redirect:/access-denied";
        }
        if (!"USER".equals(userRole)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", userRole);
        return "user/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        String userRole = (String) session.getAttribute("userRole");
        if ("USER".equals(userRole)) {
            return "redirect:/access-denied";
        }
        if (!"ADMIN".equals(userRole)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", userRole);
        return "admin/dashboard";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "Bạn không có quyền truy cập vào trang này!");
        return "error/access-denied";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "profile";
    }

    @GetMapping("/admin/settings")
    public String adminSettings(HttpSession session, Model model) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "admin/settings";
    }

    @GetMapping("/debug/users")
    @ResponseBody
    public String debugUsers() {
        try {
            StringBuilder result = new StringBuilder();
            result.append("📋 All users in database:\n\n");
            
            Iterable<User> users = userRepository.findAll();
            for (User user : users) {
                result.append("- ID: ").append(user.getId()).append("\n")
                      .append("  Name: ").append(user.getName()).append("\n")
                      .append("  Email: ").append(user.getEmail()).append("\n")
                      .append("  Password: '").append(user.getPassword()).append("' (length: ")
                      .append(user.getPassword() != null ? user.getPassword().length() : 0).append(")\n")
                      .append("  Role: ").append(user.getRole()).append("\n")
                      .append("  Username: ").append(user.getUsername()).append("\n")
                      .append("----------------------------------------\n");
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    // Tạo user test nhanh
    @GetMapping("/debug/create-test-user")
    @ResponseBody
    public String createTestUser() {
        try {
            // Tạo admin test
            User admin = new User();
            admin.setName("Admin Test");
            admin.setEmail("admin@test.com");
            admin.setPassword("123456");
            admin.setRole("ADMIN");
            userRepository.save(admin);
            
            // Tạo user test
            User user = new User();
            user.setName("User Test");
            user.setEmail("user@test.com");
            user.setPassword("123456");
            user.setRole("USER");
            userRepository.save(user);
            
            return "✅ Created test users:\n" +
                   "Admin: admin@test.com / 123456 / ADMIN\n" +
                   "User: user@test.com / 123456 / USER";
            
        } catch (Exception e) {
            return "❌ Error creating test users: " + e.getMessage();
        }
    }

    // Utility methods
    private boolean isLoggedIn(HttpSession session) {
        Object userRole = session.getAttribute("userRole");
        Object userId = session.getAttribute("userId");
        return userRole != null && userId != null;
    }

    private boolean isAdmin(HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        return "ADMIN".equals(userRole);
    }

    private boolean isUser(HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        return "USER".equals(userRole);
    }
}