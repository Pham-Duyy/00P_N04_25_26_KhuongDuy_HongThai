package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    // Kiểm tra đăng nhập user
    private boolean checkUserLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.USER;
    }

    // Trang dashboard user - hỗ trợ cả /user và /user/
    @GetMapping({"", "/", "/dashboard"})
    public String userHome(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkUserLogin(session)) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        // Thông báo mẫu - có thể lấy từ service hoặc database
        List<String> notifications = List.of(
            "Bạn đã được mời vào nhóm 'Gia đình'",
            "Giao dịch mới vừa được thêm!",
            "Chào mừng bạn đến với hệ thống quản lý ngân sách!"
        );
        model.addAttribute("notifications", notifications);

        return "user/dashboard";
    }

    // Trang thông tin cá nhân
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkUserLogin(session)) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        return "user/profile";
    }

    // Trang cài đặt tài khoản
    @GetMapping("/settings")
    public String settings(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkUserLogin(session)) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        return "user/settings";
    }

    // API để kiểm tra trạng thái đăng nhập
    @GetMapping("/api/status")
    @ResponseBody
    public String getLoginStatus(HttpSession session) {
        if (checkUserLogin(session)) {
            User user = (User) session.getAttribute("user");
            return "Đã đăng nhập với tài khoản: " + user.getUsername();
        }
        return "Chưa đăng nhập";
    }
}