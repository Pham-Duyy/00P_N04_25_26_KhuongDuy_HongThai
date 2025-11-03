package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Service.TransactionService;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class PersonalFinanceController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/personal-finance")
    public String personalFinance(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy danh sách đóng góp đã thanh toán
        List<TransactionParticipant> contributions = transactionService.findPaidContributionsByUserId(user.getId());
        BigDecimal totalAmount = contributions.stream()
                .map(TransactionParticipant::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tổng số lần đóng góp
        int totalContributions = contributions.size();

        // Tổng số nhóm đã tham gia
        int joinedGroups = groupService.findGroupsByMemberId(user.getId()).size();

        model.addAttribute("user", user);
        model.addAttribute("contributions", contributions);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalContributions", totalContributions);
        model.addAttribute("joinedGroups", joinedGroups);

        // Trả về đúng đường dẫn giao diện bạn đã tạo
        return "user/personal-finance/index";
    }
}