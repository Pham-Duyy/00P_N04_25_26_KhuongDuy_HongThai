package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Trang dashboard admin
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
        model.addAttribute("fullName", user.getFullName());
        // Có thể truyền thêm dữ liệu thống kê ở đây nếu cần
        return "admin/dashboard";
    }

    // Quản lý nhóm
    @GetMapping("/groups")
    public String manageGroups(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        return "admin/groups";
    }

    // Quản lý thu chi
    @GetMapping("/transactions")
    public String manageTransactions(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        return "admin/transactions";
    }

    // Báo cáo thống kê
    @GetMapping("/reports")
    public String viewReports(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        return "admin/reports";
    }

    // Thông báo
    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        return "admin/notifications";
    }

    // Mặc định chuyển về dashboard nếu vào /admin
    @GetMapping("")
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }
}