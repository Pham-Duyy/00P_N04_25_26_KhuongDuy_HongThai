package com.fund.group09.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
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
            } else {
                return "redirect:/dashboard";
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

    @GetMapping("/register")
    public String registerPage(@RequestParam(value = "success", required = false) String success,
                              HttpSession session,
                              Model model) {
        
        // Nếu đã đăng nhập, redirect về dashboard
        if (isLoggedIn(session)) {
            String userRole = (String) session.getAttribute("userRole");
            if ("ADMIN".equals(userRole)) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/dashboard";
            }
        }
        
        if (success != null) {
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        }
        return "auth/register";
    }

    // Dashboard cho USER
    @GetMapping("/dashboard")
    public String userDashboard(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        
        String userRole = (String) session.getAttribute("userRole");
        
        // Nếu là ADMIN thì redirect về admin dashboard
        if ("ADMIN".equals(userRole)) {
            return "redirect:/admin/dashboard";
        }
        
        // Nếu không phải USER thì từ chối truy cập
        if (!"USER".equals(userRole)) {
            return "redirect:/access-denied";
        }
        
        // Thêm thông tin user vào model
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("userId", session.getAttribute("userId"));
        model.addAttribute("userRole", userRole);
        
        return "dashboard"; // Giao diện user
    }

    // Dashboard cho ADMIN  
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (!isLoggedIn(session)) {
            return "redirect:/login?error=notLoggedIn";
        }
        
        String userRole = (String) session.getAttribute("userRole");
        
        // Nếu là USER thì redirect về user dashboard
        if ("USER".equals(userRole)) {
            return "redirect:/dashboard";
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
    // ...existing code...

    // Trang logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ session
        // Chuyển hướng về trang đăng nhập (không kiểm tra đăng nhập nữa)
        return "redirect:/login?logout";
    }

// ...existing code...
    
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