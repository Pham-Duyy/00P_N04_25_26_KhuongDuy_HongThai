package com.fund.group09.Controller;

import com.fund.group09.Model.Group;
import com.fund.group09.Model.Expense;
import com.fund.group09.Model.Income;
import com.fund.group09.Repository.GroupRepository;
import com.fund.group09.Repository.ExpenseRepository;
import com.fund.group09.Repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/admin/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired 
    private IncomeRepository incomeRepository;

    // Trang quản lý nhóm với tìm kiếm và phân trang
    @GetMapping
public String index(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "5") int size,
        HttpSession session,
        Model model,
        @ModelAttribute("inviteMessage") String inviteMessage
) {
    if (!isLoggedIn(session) || !isAdmin(session)) {
        return "redirect:/login?error=access_denied";
    }

    Pageable pageable = PageRequest.of(page, size);
    Page<Group> groupPage;
    if (keyword != null && !keyword.trim().isEmpty()) {
        groupPage = groupRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
        model.addAttribute("keyword", keyword);
    } else {
        groupPage = groupRepository.findAll(pageable);
    }

    List<Group> groups = groupPage.getContent();
    model.addAttribute("groups", groups);
    model.addAttribute("groupPage", groupPage);
    model.addAttribute("totalGroups", groupPage.getTotalElements());

    // Tổng thành viên và tổng quỹ (trên tất cả các nhóm)
    // Tổng thành viên và tổng quỹ (trên tất cả các nhóm)
int totalMembers = 0;
double totalFund = 0;
int activeGroups = 0;
for (Group g : groups) {
    if (g.getMembers() != null) {
        totalMembers += g.getMembers().size();
    }
    if (g.getFund() != null) {
        Double balance = g.getFund().getBalance();
        if (balance != null) {
            totalFund += balance;
        }
    }
    if (Boolean.TRUE.equals(g.getIsActive())) {
        activeGroups++;
    }
}
    model.addAttribute("totalMembers", totalMembers);
    model.addAttribute("totalFund", totalFund);
    model.addAttribute("activeGroups", activeGroups);

    // Định dạng ngày tạo cho từng group (nếu muốn show chuỗi format sẵn)
    Map<Long, String> groupCreatedDates = new HashMap<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    for (Group g : groups) {
        if (g.getCreatedDate() != null) {
            groupCreatedDates.put(g.getId(), g.getCreatedDate().format(formatter));
        }
    }
    model.addAttribute("groupCreatedDates", groupCreatedDates);

    // Thông báo mời tham gia nhóm (nếu có)
    if (inviteMessage != null && !inviteMessage.isEmpty()) {
        model.addAttribute("inviteMessage", inviteMessage);
    }

    // Truyền param để giữ lại keyword khi chuyển trang
    model.addAttribute("param", Map.of(
        "keyword", keyword != null ? keyword : "",
        "page", page,
        "size", size
    ));

    return "admin/groups/index";
}

    // Tạo nhóm mới (form ngay trên trang index)
    @PostMapping("/create")
    public String createGroup(@ModelAttribute Group group,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }
        // Validate tên nhóm
        if (group.getName() == null || group.getName().trim().length() < 3 || group.getName().trim().length() > 50) {
            redirectAttributes.addFlashAttribute("error", "Tên nhóm phải từ 3 đến 50 ký tự!");
            return "redirect:/admin/groups";
        }
        try {
            // Nếu Group thiếu các trường như createdDate, joinCode, maxMembers... thì nên set ở đây
            if (group.getCreatedDate() == null) {
                group.setCreatedDate(java.time.LocalDateTime.now());
            }
            if (group.getJoinCode() == null || group.getJoinCode().isEmpty()) {
                group.setJoinCode(generateJoinCode());
            }
            if (group.getMaxMembers() == null) {
                group.setMaxMembers(50);
            }
            if (group.getIsActive() == null) {
                group.setIsActive(true);
            }
            if (group.getGroupType() == null) {
                group.setGroupType("PUBLIC");
            }
            groupRepository.save(group);

            // Gửi thông báo mời sang user (ví dụ: gửi sang trang join hoặc hiển thị ở trang index)
            String inviteMsg = "Nhóm \"" + group.getName() + "\" đã được tạo. Mã mời: " + group.getJoinCode() + ". Hãy gửi mã này cho user để tham gia nhóm!";
            redirectAttributes.addFlashAttribute("inviteMessage", inviteMsg);
            redirectAttributes.addFlashAttribute("success", "Tạo nhóm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo nhóm!");
        }
        return "redirect:/admin/groups";
    }

    // Hiển thị form chỉnh sửa nhóm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return "redirect:/admin/groups?error=group_not_found";
        }
        model.addAttribute("group", groupOpt.get());
        return "admin/groups/edit";
    }

    // Cập nhật nhóm
    @PostMapping("/edit/{id}")
    public String updateGroup(@PathVariable Long id,
                              @ModelAttribute Group newGroup,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }
        // Validate tên nhóm
        if (newGroup.getName() == null || newGroup.getName().trim().length() < 3 || newGroup.getName().trim().length() > 50) {
            redirectAttributes.addFlashAttribute("error", "Tên nhóm phải từ 3 đến 50 ký tự!");
            return "redirect:/admin/groups/edit/" + id;
        }
        try {
            Optional<Group> groupOpt = groupRepository.findById(id);
            if (groupOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhóm!");
                return "redirect:/admin/groups";
            }
            Group group = groupOpt.get();
            group.setName(newGroup.getName());
            group.setDescription(newGroup.getDescription());
            group.setIsActive(newGroup.getIsActive());
            group.setMaxMembers(newGroup.getMaxMembers());
            group.setGroupType(newGroup.getGroupType());
            groupRepository.save(group);
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhóm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật nhóm!");
        }
        return "redirect:/admin/groups";
    }

    // Xem chi tiết nhóm
    @GetMapping("/detail/{id}")
    public String viewGroupDetail(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return "redirect:/admin/groups?error=group_not_found";
        }
        Group group = groupOpt.get();
        Map<String, Object> summary = getGroupSummaryData(id);
        model.addAttribute("group", group);
        model.addAttribute("summary", summary);
        return "admin/groups/detail";
    }

    // Xóa nhóm
    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (!isLoggedIn(session) || !isAdmin(session)) {
            return "redirect:/login?error=access_denied";
        }
        try {
            if (!groupRepository.existsById(id)) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhóm!");
                return "redirect:/admin/groups";
            }
            groupRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa nhóm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa nhóm! Có thể nhóm đang có dữ liệu liên quan.");
        }
        return "redirect:/admin/groups";
    }

    // API: Lấy tất cả nhóm (cho AJAX)
    @GetMapping("/api/all")
    @ResponseBody
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // API: Lấy nhóm theo ID (cho AJAX)
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API: Thống kê nhóm (cho AJAX)
    @GetMapping("/api/{id}/summary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGroupSummary(@PathVariable Long id) {
        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> summary = getGroupSummaryData(id);
        return ResponseEntity.ok(summary);
    }

    // Phương thức hỗ trợ: Tính toán thống kê nhóm
    private Map<String, Object> getGroupSummaryData(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            return new HashMap<>();
        }
        Group group = groupOpt.get();
        List<Expense> expenses = expenseRepository.findAll();
        List<Income> incomes = incomeRepository.findAll();

        double totalExpense = expenses.stream()
                .filter(e -> e.getMember() != null && e.getMember().getGroup() != null
                        && e.getMember().getGroup().getId().equals(groupId))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalIncome = incomes.stream()
                .filter(i -> i.getMember() != null && i.getMember().getGroup() != null
                        && i.getMember().getGroup().getId().equals(groupId))
                .mapToDouble(Income::getAmount)
                .sum();

        double currentBalance = totalIncome - totalExpense;

        Map<String, Object> summary = new HashMap<>();
        summary.put("groupName", group.getName());
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("currentBalance", currentBalance);
        summary.put("membersCount", group.getMembers() != null ? group.getMembers().size() : 0);

        return summary;
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

    // Sinh mã joinCode ngẫu nhiên
    private String generateJoinCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}