package com.fund.group09.Controller;

import com.fund.group09.Model.Group;
import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import com.fund.group09.Service.CategoryService;
import com.fund.group09.Service.GroupService;
import com.fund.group09.Service.TransactionService;
import com.fund.group09.Service.UserService;
import com.fund.group09.Service.NotificationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fund.group09.Model.Category;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/admin/finance")
public class TransactionController {

@Autowired
private CategoryService categoryService;


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String showTransactionPage(Model model,
                                     @RequestParam(required = false) Long groupId,
                                     @RequestParam(required = false) String status,
                                     @RequestParam(required = false) Long newTx,
                                     HttpSession session) {
        // Kiểm tra quyền ADMIN
        if (!isAdmin(session)) return "redirect:/login";
        try {
            List<Group> groups = groupService.getAllGroups();
            model.addAttribute("groups", groups);

            if (newTx != null) {
                try {
                    transactionService.refreshTransactionCache();
                } catch (Exception e) {
                    // ignore
                }
            }

            List<Transaction> transactions = getFilteredTransactions(groupId, status);
            model.addAttribute("transactions", transactions);

            if (newTx != null && transactions != null) {
                boolean found = transactions.stream().anyMatch(tx -> tx.getId().equals(newTx));
                if (found) {
                    model.addAttribute("highlightTransactionId", newTx);
                }
            }

            if (groupId != null) {
                Group selectedGroup = groupService.findById(groupId);
                model.addAttribute("selectedGroup", selectedGroup);
                model.addAttribute("selectedGroupId", groupId);
            }
            if (status != null && !status.isEmpty()) {
                model.addAttribute("selectedStatus", status);
            }

            calculateStatistics(model, groupId);

            return "admin/finance/transaction";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải dữ liệu: " + e.getMessage());
            model.addAttribute("transactions", List.of());
            model.addAttribute("groups", List.of());
            return "admin/finance/transaction";
        }
    }

    private List<Transaction> getFilteredTransactions(Long groupId, String status) {
        try {
            List<Transaction> result;
            if (groupId != null && status != null && !status.isEmpty()) {
                result = transactionService.getTransactionsByGroupAndStatus(groupId, status);
            } else if (groupId != null) {
                result = transactionService.getTransactionsByGroup(groupId);
            } else if (status != null && !status.isEmpty()) {
                result = transactionService.getTransactionsByGroupAndStatus(null, status);
            } else {
                result = transactionService.getAllTransactions();
            }
            return result != null ? result : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }

    @GetMapping("/income/new")
    public String showCreateIncomeForm(Model model, @RequestParam(required = false) Long groupId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", "INCOME");
        if (groupId != null) {
            model.addAttribute("selectedGroupId", groupId);
        }
        return "admin/finance/transaction-form";
    }

    @GetMapping("/expense/new")
    public String showCreateExpenseForm(Model model, @RequestParam(required = false) Long groupId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", "EXPENSE");
        if (groupId != null) {
            model.addAttribute("selectedGroupId", groupId);
        }
        return "admin/finance/transaction-form";
    }

@PostMapping("/create")
public String createTransaction(@ModelAttribute("transaction") Transaction transaction,
                               @RequestParam("groupId") Long groupId,
                               @RequestParam("type") String type,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
    
    if (!isAdmin(session)) {
        return "redirect:/login";
    }

    Long userId = (Long) session.getAttribute("userId");
    User admin = userService.findById(userId);

    // VALIDATION
    if (groupId == null) {
        model.addAttribute("error", "Vui lòng chọn nhóm!");
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", type);
        return "admin/finance/transaction-form";
    }

    if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.valueOf(1000)) < 0) {
        model.addAttribute("error", "Vui lòng nhập số tiền hợp lệ (tối thiểu 1,000 VNĐ)!");
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", type);
        model.addAttribute("selectedGroupId", groupId);
        return "admin/finance/transaction-form";
    }

    if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
        model.addAttribute("error", "Vui lòng nhập mô tả giao dịch!");
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", type);
        model.addAttribute("selectedGroupId", groupId);
        return "admin/finance/transaction-form";
    }

    try {
        Group group = groupService.findById(groupId);
        if (group == null) {
            model.addAttribute("error", "Không tìm thấy nhóm!");
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("type", type);
            model.addAttribute("selectedGroupId", groupId);
            return "admin/finance/transaction-form";
        }

        // ✅ FIX: ĐẢM BẢO CATEGORY KHÔNG NULL - BẮT BUỘC PHẢI CÓ
        if (transaction.getCategory() == null) {
            Category defaultCategory = categoryService.findById(4L)
                .orElseGet(() -> {
                    // Nếu không có category id=4, lấy bất kỳ category nào
                    List<Category> allCategories = categoryService.findAll();
                    if (!allCategories.isEmpty()) {
                        return allCategories.get(0);
                    }
                    // Nếu không có category nào, tạo mới
                    Category newCategory = new Category();
                    newCategory.setName("Khác");
                    newCategory.setDescription("Danh mục mặc định");
                    newCategory.setType("EXPENSE");  // ← SỬA THÀNH STRING
                    return categoryService.save(newCategory);
                });
            
            transaction.setCategory(defaultCategory);
            System.out.println("✅ Controller: Set category mặc định - " + defaultCategory.getName() + " (ID: " + defaultCategory.getId() + ")");
        }
        // SET THÔNG TIN GIAO DỊCH
        LocalDateTime now = LocalDateTime.now();
        transaction.setGroup(group);
        transaction.setCreatedBy(admin);
        transaction.setType(Transaction.TransactionType.valueOf(type.toUpperCase()));
        transaction.setDate(now);
        transaction.setCreatedDate(now);
        transaction.setUpdatedDate(now);
        transaction.setStatus(Transaction.Status.COMPLETED);
        transaction.setApproved(true);
        transaction.setPayer(admin); 
        // LOG để debug
        System.out.println("🔍 DEBUG trước khi gọi service:");
        System.out.println("   - Category: " + (transaction.getCategory() != null ? transaction.getCategory().getName() : "NULL"));
        System.out.println("   - Category ID: " + (transaction.getCategory() != null ? transaction.getCategory().getId() : "NULL"));
        System.out.println("   - Amount: " + transaction.getAmount());
        System.out.println("   - Description: " + transaction.getDescription());
        System.out.println("   - Type: " + transaction.getType());
        System.out.println("   - Group: " + transaction.getGroup().getName());

        // LƯU TRANSACTION
        Transaction savedTransaction = transactionService.createTransactionWithGroup(
            transaction, group, Transaction.TransactionType.valueOf(type.toUpperCase()));

        if (savedTransaction == null || savedTransaction.getId() == null) {
            throw new RuntimeException("Không thể lưu giao dịch vào database");
        }

        // GỬI THÔNG BÁO
        try {
            if ("INCOME".equals(type)) {
                notificationService.sendIncomeRequestNotification(savedTransaction, group);
            } else {
                notificationService.sendExpenseNotification(savedTransaction, group);
            }
        } catch (Exception notifEx) {
            System.out.println("Lỗi gửi thông báo: " + notifEx.getMessage());
        }

        String actionText = type.equals("INCOME") ? "yêu cầu đóng tiền" : "giao dịch chi";
        String successMsg = String.format("✅ Đã tạo và hoàn thành %s thành công: %s (ID: %d)",
            actionText,
            transaction.getDescription(),
            savedTransaction.getId());

        redirectAttributes.addFlashAttribute("successMessage", successMsg);
        redirectAttributes.addFlashAttribute("newTransactionId", savedTransaction.getId());

        return "redirect:/admin/finance?groupId=" + groupId + "&newTx=" + savedTransaction.getId();

    } catch (Exception e) {
        System.out.println("Lỗi tạo giao dịch: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("type", type);
        model.addAttribute("selectedGroupId", groupId);
        return "admin/finance/transaction-form";
    }
}
    @GetMapping("/detail/{id}")
    public String viewTransactionDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            Transaction transaction = transactionService.findById(id);
            model.addAttribute("transaction", transaction);
            return "admin/finance/transaction-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Không tìm thấy giao dịch: " + e.getMessage());
            return "redirect:/admin/finance";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTransactionForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("userId");
        User admin = userService.findById(userId);

        try {
            Transaction transaction = transactionService.findById(id);
            model.addAttribute("transaction", transaction);
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("type", transaction.getType().name());
            model.addAttribute("selectedGroupId",
                transaction.getGroup() != null ? transaction.getGroup().getId() : null);
            return "admin/finance/transaction-edit-form";
        } catch (Exception e) {
            model.addAttribute("error", "Không thể chỉnh sửa: " + e.getMessage());
            return "redirect:/admin/finance";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateTransaction(@PathVariable Long id,
                                   @Valid @ModelAttribute("transaction") Transaction transaction,
                                   BindingResult result,
                                   @RequestParam("groupId") Long groupId,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("userId");
        User admin = userService.findById(userId);

        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("type", transaction.getType().name());
            model.addAttribute("selectedGroupId", groupId);
            return "admin/finance/transaction-edit-form";
        }

        try {
            transaction.setId(id);
            Group group = groupService.findById(groupId);
            if (group == null) {
                model.addAttribute("error", "Không tìm thấy nhóm!");
                model.addAttribute("groups", groupService.getAllGroups());
                return "admin/finance/transaction-edit-form";
            }

            transaction.setGroup(group);
            transactionService.updateTransaction(transaction);

            redirectAttributes.addFlashAttribute("successMessage",
                "✅ Đã cập nhật giao dịch thành công!");

            return "redirect:/admin/finance?groupId=" + groupId;
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("groups", groupService.getAllGroups());
            model.addAttribute("type", transaction.getType().name());
            model.addAttribute("selectedGroupId", groupId);
            return "admin/finance/transaction-edit-form";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(@PathVariable Long id,
                                   @RequestParam(required = false) Long groupId,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        Long userId = (Long) session.getAttribute("userId");
        User admin = userService.findById(userId);

        try {
            Transaction transaction = transactionService.findById(id);
            String description = transaction.getDescription();

            transactionService.deleteTransaction(id);

            redirectAttributes.addFlashAttribute("successMessage",
                "✅ Đã xóa giao dịch: " + description);

            if (groupId != null) {
                return "redirect:/admin/finance?groupId=" + groupId;
            }
            return "redirect:/admin/finance";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                "❌ Không thể xóa giao dịch: " + e.getMessage());
            return "redirect:/admin/finance" + (groupId != null ? "?groupId=" + groupId : "");
        }
    }

    @GetMapping("/history")
    public String showAdminTransactionHistory(Model model,
                                              @RequestParam(required = false) Long groupId,
                                              @RequestParam(required = false) String status,
                                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            List<Group> groups = groupService.getAllGroups();
            model.addAttribute("groups", groups);

            List<Transaction> transactions = getFilteredTransactions(groupId, status);
            model.addAttribute("transactions", transactions);

            if (groupId != null) {
                model.addAttribute("selectedGroupId", groupId);
            }
            if (status != null && !status.isEmpty()) {
                model.addAttribute("selectedStatus", status);
            }

            calculateStatistics(model, groupId);

            return "admin/finance/history";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "admin/finance/history";
        }
    }

    @GetMapping("/api/statistics")
    @ResponseBody
    public Map<String, Object> getStatistics(@RequestParam(required = false) Long groupId) {
        Map<String, Object> stats = new HashMap<>();
        try {
            BigDecimal totalIncome = transactionService.getTotalIncomeByGroup(groupId);
            BigDecimal totalExpense = transactionService.getTotalExpenseByGroup(groupId);
            BigDecimal balance = transactionService.getBalanceByGroup(groupId);

            List<Transaction> transactions = groupId != null ?
                transactionService.getTransactionsByGroup(groupId) :
                transactionService.getAllTransactions();

            stats.put("totalIncome", totalIncome != null ? totalIncome.doubleValue() : 0.0);
            stats.put("totalExpense", totalExpense != null ? totalExpense.doubleValue() : 0.0);
            stats.put("balance", balance != null ? balance.doubleValue() : 0.0);
            stats.put("transactionCount", transactions.size());
            stats.put("incomeCount", transactions.stream()
                .mapToInt(tx -> tx.getType() == Transaction.TransactionType.INCOME ? 1 : 0).sum());
            stats.put("expenseCount", transactions.stream()
                .mapToInt(tx -> tx.getType() == Transaction.TransactionType.EXPENSE ? 1 : 0).sum());
            stats.put("success", true);
        } catch (Exception e) {
            stats.put("success", false);
            stats.put("error", e.getMessage());
        }
        return stats;
    }

    @GetMapping("/api/group/{id}")
    @ResponseBody
    public Group getGroupInfo(@PathVariable Long id) {
        try {
            return groupService.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    // Utility method kiểm tra quyền ADMIN từ session
    private boolean isAdmin(HttpSession session) {
        Object userRole = session.getAttribute("userRole");
        Object userId = session.getAttribute("userId");
        return userRole != null && userId != null && "ADMIN".equals(userRole);
    }
    
    private void calculateStatistics(Model model, Long groupId) {
        try {
            BigDecimal totalIncome = transactionService.getTotalIncomeByGroup(groupId);
            BigDecimal totalExpense = transactionService.getTotalExpenseByGroup(groupId);
            BigDecimal balance = transactionService.getBalanceByGroup(groupId);

            model.addAttribute("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
            model.addAttribute("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
            model.addAttribute("balance", balance != null ? balance : BigDecimal.ZERO);
        } catch (Exception e) {
            model.addAttribute("totalIncome", BigDecimal.ZERO);
            model.addAttribute("totalExpense", BigDecimal.ZERO);
            model.addAttribute("balance", BigDecimal.ZERO);
        }
    }
}