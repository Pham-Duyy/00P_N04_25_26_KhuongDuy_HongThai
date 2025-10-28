package com.fund.group09.Controller;

import com.fund.group09.Model.User;
import com.fund.group09.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Thêm import này
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
    private PasswordEncoder passwordEncoder; // Thêm PasswordEncoder

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

    // Xử lý POST đăng nhập
    @PostMapping("/login")
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("role") String role,
                          HttpSession session,
                          Model model) {
        // Kiểm tra tài khoản thực tế trong DB
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Tài khoản không tồn tại!");
            return "auth/login";
        }
        User user = optionalUser.get();

        // So sánh mật khẩu (dùng PasswordEncoder nếu mật khẩu đã mã hóa)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Sai mật khẩu!");
            return "auth/login";
        }

        // Kiểm tra vai trò thực tế của tài khoản
        if (!user.getRole().equalsIgnoreCase(role)) {
            model.addAttribute("error", "Bạn không có quyền đăng nhập với vai trò này!");
            return "auth/login";
        }

        // Đăng nhập thành công, lưu thông tin vào session
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userId", user.getId());
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else if ("USER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/user/dashboard";
        }
        model.addAttribute("error", "Vai trò không hợp lệ!");
        return "auth/login";
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

    // Dashboard cho USER
    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        String userRole = (String) session.getAttribute("userRole");
        // Nếu là ADMIN thì không cho vào trang user
        if ("ADMIN".equals(userRole)) {
            return "redirect:/access-denied";
        }
        // Nếu không phải USER thì từ chối truy cập
        if (!"USER".equals(userRole)) {
            return "redirect:/access-denied";
        }
        // Thêm thông tin user vào model
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userRole", userRole);
        return "user/dashboard"; // Giao diện user
    }

    // Dashboard cho ADMIN  
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        String userRole = (String) session.getAttribute("userRole");
        // Nếu là USER thì không cho vào trang admin
        if ("USER".equals(userRole)) {
            return "redirect:/access-denied";
        }
        // Nếu không phải ADMIN thì từ chối truy cập
        if (!"ADMIN".equals(userRole)) {
            return "redirect:/access-denied";
        }
        // Thêm thông tin admin vào model
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userRole", userRole);
        return "admin/dashboard"; // Giao diện admin
    }

    // Trang từ chối truy cập
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "Bạn không có quyền truy cập vào trang này!");
        return "error/access-denied";
    }
    
    // Trang logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        // Chuyển hướng về trang đăng nhập (không kiểm tra đăng nhập nữa)
        return "redirect:/login?logout";
    }

    // Utility method kiểm tra đăng nhập
    private boolean isLoggedIn(HttpSession session) {
        Object userRole = session.getAttribute("userRole");
        Object userId = session.getAttribute("userId");
        return userRole != null && userId != null;
    }
    
    // Utility method kiểm tra quyền admin
    private boolean isAdmin(HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        return "ADMIN".equals(userRole);
    }
    
    // Utility method kiểm tra quyền user
    private boolean isUser(HttpSession session) {
        String userRole = (String) session.getAttribute("userRole");
        return "USER".equals(userRole);
    }
    
    // Route cho profile user
    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "profile";
    }
    
    // Route cho settings (chỉ admin)
    @GetMapping("/admin/settings")
    public String adminSettings(HttpSession session, Model model) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "admin/settings";
    }
}