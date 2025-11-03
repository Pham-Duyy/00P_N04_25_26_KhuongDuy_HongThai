package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.ReportService;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.TransactionService;
import com.oop.quanlingansach.Service.TransactionParticipantService;
import com.oop.quanlingansach.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionParticipantService transactionParticipantService;

    @Autowired
    private UserService userService;

    // Trang báo cáo tổng quan
    @GetMapping
    public String reportOverview(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }

        // Thống kê tổng quan
        long totalGroups = groupService.countAllGroups();
        long totalTransactions = transactionService.countAll();
        // Chỉ đếm user có vai trò là USER (không tính admin)
        long totalUsers = userService.countAllNormalUsers();
        BigDecimal totalAmount = reportService.getTotalTransactionAmount();

        // Thống kê theo loại giao dịch
        long incomeTransactions = transactionService.countByType("INCOME");
        long expenseTransactions = transactionService.countByType("EXPENSE");
        BigDecimal totalIncomeAmount = reportService.getTotalAmountByType("INCOME");
        BigDecimal totalExpenseAmount = reportService.getTotalAmountByType("EXPENSE");

        // Thống kê đóng góp
        long totalContributions = reportService.countAllContributions();
        long paidContributions = reportService.countPaidContributions();
        long unpaidContributions = totalContributions - paidContributions;

        model.addAttribute("totalGroups", totalGroups);
        model.addAttribute("totalTransactions", totalTransactions);
        model.addAttribute("totalUsers", totalUsers); // Chỉ user thường
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("incomeTransactions", incomeTransactions);
        model.addAttribute("expenseTransactions", expenseTransactions);
        model.addAttribute("totalIncomeAmount", totalIncomeAmount);
        model.addAttribute("totalExpenseAmount", totalExpenseAmount);
        model.addAttribute("totalContributions", totalContributions);
        model.addAttribute("paidContributions", paidContributions);
        model.addAttribute("unpaidContributions", unpaidContributions);

        return "admin/reports/index";
    }

    // Báo cáo chi tiết nhóm
    @GetMapping("/groups")
    public String groupReport(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }

        List<Map<String, Object>> groupStatistics = reportService.getGroupStatistics();

        model.addAttribute("groupStatistics", groupStatistics);
        return "admin/reports/groups";
    }

    // Báo cáo chi tiết giao dịch
    @GetMapping("/transactions")
    public String transactionReport(HttpSession session, Model model,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String status) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }

        List<Map<String, Object>> transactionStatistics = reportService.getTransactionStatistics(type, status);

        model.addAttribute("transactionStatistics", transactionStatistics);
        model.addAttribute("filterType", type);
        model.addAttribute("filterStatus", status);
        return "admin/reports/transactions";
    }

    // Báo cáo đóng góp của thành viên
    @GetMapping("/contributions")
public String contributionReport(HttpSession session, Model model,
                                 @RequestParam(required = false) Long groupId) {
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRole() != User.Role.ADMIN) {
        return "redirect:/login";
    }

    List<Map<String, Object>> contributionStatistics = reportService.getContributionStatistics(groupId);

    // Tính tổng tiền ở đây
    BigDecimal totalAmount = BigDecimal.ZERO;
    for (Map<String, Object> stat : contributionStatistics) {
        Object amountObj = stat.get("amount");
        if (amountObj instanceof BigDecimal) {
            totalAmount = totalAmount.add((BigDecimal) amountObj);
        } else if (amountObj != null) {
            try {
                totalAmount = totalAmount.add(new BigDecimal(amountObj.toString()));
            } catch (Exception ignored) {}
        }
    }

    var groups = groupService.findAll();

    model.addAttribute("contributionStatistics", contributionStatistics);
    model.addAttribute("groups", groups);
    model.addAttribute("selectedGroupId", groupId);
    model.addAttribute("totalContributionAmount", totalAmount); // truyền ra view
    return "admin/reports/contributions";
}

// ...existing code...
    // Báo cáo thu chi chi tiết theo nhóm
    @GetMapping("/expenses")
    public String groupExpenseReport(
            HttpSession session,
            Model model,
            @RequestParam(required = false) Long groupId
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }

        // Lấy danh sách nhóm để chọn lọc
        var groups = groupService.findAll();
        model.addAttribute("groups", groups);
        model.addAttribute("selectedGroupId", groupId);

        if (groupId != null) {
            // Lấy thông tin nhóm
            var group = groupService.findById(groupId);
            model.addAttribute("group", group);

            // Tổng quỹ mục tiêu ban đầu (nếu có)
            model.addAttribute("targetAmount", group.getTargetAmount());

            // Tổng số tiền thành viên đã đóng góp thực tế (đã xác nhận)
            BigDecimal totalContributed = transactionParticipantService.getTotalPaidAmountByGroup(groupId);
            model.addAttribute("totalContributed", totalContributed);

            // Tổng số tiền đã chi (từ các giao dịch chi của nhóm)
            BigDecimal totalExpense = transactionService.getTotalExpenseByGroup(groupId);
            model.addAttribute("totalExpense", totalExpense);

            // Số dư quỹ hiện tại = tổng đóng góp thực tế - tổng chi
            BigDecimal currentFund = totalContributed.subtract(totalExpense != null ? totalExpense : BigDecimal.ZERO);
            model.addAttribute("currentFund", currentFund);

            // Lịch sử chi (danh sách giao dịch chi)
            var expenseHistory = transactionService.getExpenseHistoryByGroup(groupId);
            model.addAttribute("expenseHistory", expenseHistory);

            // Lịch sử đóng góp (danh sách các đóng góp đã xác nhận)
            var contributionHistory = transactionParticipantService.getPaidContributionsByGroup(groupId);
            model.addAttribute("contributionHistory", contributionHistory);
        }

        return "admin/reports/expenses";
    }
// ...existing code...

    // API endpoint để lấy dữ liệu biểu đồ (JSON)
    @GetMapping("/api/chart-data")
    public String getChartData(HttpSession session, Model model,
                               @RequestParam String chartType) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }

        Object chartData;
        switch (chartType) {
            case "transaction-by-month":
                chartData = reportService.getTransactionByMonthData();
                break;
            case "group-activity":
                chartData = reportService.getGroupActivityData();
                break;
            case "payment-status":
                chartData = reportService.getPaymentStatusData();
                break;
            default:
                chartData = null;
        }

        model.addAttribute("chartData", chartData);
        return "admin/reports/chart-data";
    }
}