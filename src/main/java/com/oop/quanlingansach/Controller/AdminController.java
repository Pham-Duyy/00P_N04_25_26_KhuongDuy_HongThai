package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.TransactionService;
import com.oop.quanlingansach.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TransactionService transactionService;


    // Trang dashboard admin
   @GetMapping("/dashboard")
public String adminDashboard(Model model, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.ADMIN) {
        return "redirect:/login";
    }
    model.addAttribute("user", user);

    // Đếm số người dùng có vai trò USER
    long totalUsers = userService.countByRole(User.Role.USER);
    model.addAttribute("totalUsers", totalUsers);

    // Tổng số nhóm đã tạo
    model.addAttribute("activeGroups", groupService.findAll().size());

    // Tổng số giao dịch thu
    long totalIncomeTransactions = transactionService.countByType("INCOME");
    model.addAttribute("totalIncomeTransactions", totalIncomeTransactions);

    // Tổng số giao dịch chi
    long totalExpenseTransactions = transactionService.countByType("EXPENSE");
    model.addAttribute("totalExpenseTransactions", totalExpenseTransactions);

    return "admin/dashboard";
}

    // Quản lý thu chi
    @GetMapping("/transactions")
    public String manageTransactions(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin/transactions";
    }

    // Báo cáo thống kê
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin/reports";
    }

    // Thông báo
    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin/notifications";
    }

    // Mặc định chuyển về dashboard nếu vào /admin
    @GetMapping("")
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }
}