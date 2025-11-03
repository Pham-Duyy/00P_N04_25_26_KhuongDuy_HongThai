package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.TransactionService;

import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private TransactionService transactionService;


    // Kiểm tra đăng nhập user
    private boolean checkUserLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == User.Role.USER;
    }

        // Trang dashboard user
    @GetMapping({"", "/", "/dashboard"})
    public String userHome(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!checkUserLogin(session)) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        // Tổng số nhóm đã tham gia
        int joinedGroups = groupService.findGroupsByMemberId(user.getId()).size();
        model.addAttribute("joinedGroups", joinedGroups);

        // Tổng số giao dịch đóng góp (đã xác nhận thanh toán)
        List<TransactionParticipant> paidContributions = transactionService.findPaidContributionsByUserId(user.getId());
        int totalContributions = paidContributions.size();
        model.addAttribute("totalContributions", totalContributions);

        // Tổng số tiền giao dịch đã xác nhận thanh toán
        BigDecimal totalPaidAmount = paidContributions.stream()
                .map(TransactionParticipant::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalPaidAmount", totalPaidAmount);

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