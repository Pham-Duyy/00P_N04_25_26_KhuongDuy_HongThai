package com.fund.group09.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;

// Giả lập model Invitation cho demo
class Invitation {
    public Long id;
    public String groupName;
    public String invitedBy;
    public String invitedDate;
    public int memberCount;
    public String description;
    public String status; // PENDING, ACCEPTED, REJECTED
    public String responseDate;

    public Invitation(Long id, String groupName, String invitedBy, String invitedDate, int memberCount, String description, String status, String responseDate) {
        this.id = id;
        this.groupName = groupName;
        this.invitedBy = invitedBy;
        this.invitedDate = invitedDate;
        this.memberCount = memberCount;
        this.description = description;
        this.status = status;
        this.responseDate = responseDate;
    }
}

@Controller
@RequestMapping("/user/groups")
public class UserGroupController {

    // Trang danh sách lời mời tham gia nhóm
    @GetMapping("/join")
    public String joinGroupPage(HttpSession session, Model model) {
        // Dữ liệu mẫu cho demo
        List<Invitation> pendingInvitations = new ArrayList<>();
        pendingInvitations.add(new Invitation(1L, "Nhóm Du lịch Hè 2024", "Nguyễn Văn A", "15/10/2024", 8, "Nhóm quản lý chi phí cho chuyến du lịch hè năm nay", "PENDING", null));
        List<Invitation> acceptedInvitations = new ArrayList<>();
        acceptedInvitations.add(new Invitation(2L, "Nhóm Học tập IT", "Trần Thị B", "10/10/2024", 5, "Nhóm học tập công nghệ thông tin", "ACCEPTED", "12/10/2024"));
        List<Invitation> rejectedInvitations = new ArrayList<>();
        rejectedInvitations.add(new Invitation(3L, "Nhóm Bóng đá", "Lê Văn C", "08/10/2024", 12, "Nhóm đá bóng cuối tuần", "REJECTED", "09/10/2024"));

        // Lịch sử gần đây (gộp accepted + rejected)
        List<Invitation> recentInvitations = new ArrayList<>();
        recentInvitations.addAll(acceptedInvitations);
        recentInvitations.addAll(rejectedInvitations);

        model.addAttribute("pendingInvitations", pendingInvitations);
        model.addAttribute("acceptedInvitations", acceptedInvitations);
        model.addAttribute("rejectedInvitations", rejectedInvitations);
        model.addAttribute("recentInvitations", recentInvitations);
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        return "user/groups/join";
    }

    // Chấp nhận lời mời tham gia nhóm (AJAX)
    @PostMapping("/invitations/{invitationId}/accept")
    @ResponseBody
    public Map<String, Object> acceptInvitation(@PathVariable("invitationId") Long invitationId, HttpSession session) {
        // TODO: Xử lý chấp nhận lời mời thực tế
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Đã chấp nhận lời mời nhóm!");
        return result;
    }

    // Từ chối lời mời tham gia nhóm (AJAX)
    @PostMapping("/invitations/{invitationId}/reject")
    @ResponseBody
    public Map<String, Object> rejectInvitation(@PathVariable("invitationId") Long invitationId, HttpSession session) {
        // TODO: Xử lý từ chối lời mời thực tế
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Đã từ chối lời mời nhóm!");
        return result;
    }

    // Trang tìm kiếm nhóm
    @GetMapping("/search")
    public String searchGroupPage(HttpSession session, Model model) {
        // TODO: Lấy danh sách nhóm có thể tìm kiếm
        return "user/groups/search";
    }

    // Trang tạo nhóm mới
    @GetMapping("/create")
    public String createGroupPage(HttpSession session, Model model) {
        // TODO: Hiển thị form tạo nhóm
        return "user/groups/create";
    }

    // Xử lý tạo nhóm mới
    @PostMapping("/create")
    public String doCreateGroup(@RequestParam("groupName") String groupName,
                                @RequestParam("description") String description,
                                HttpSession session,
                                Model model) {
        // TODO: Xử lý tạo nhóm mới thực tế
        // Sau khi tạo thành công, chuyển hướng về trang nhóm
        return "redirect:/user/groups";
    }

    // Trang danh sách nhóm của user
    @GetMapping
    public String listGroups(HttpSession session, Model model) {
        // TODO: Lấy danh sách nhóm của user
        return "user/groups/list";
    }
}