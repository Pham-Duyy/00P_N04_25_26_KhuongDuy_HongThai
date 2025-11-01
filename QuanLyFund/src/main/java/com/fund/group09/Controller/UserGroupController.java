package com.fund.group09.Controller;

import com.fund.group09.Model.*;
import com.fund.group09.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user/groups")
public class UserGroupController {

    private final InvitationService invitationService;
    private final GroupService groupService;
    private final MemberService memberService;

    public UserGroupController(
            InvitationService invitationService, 
            GroupService groupService,
            MemberService memberService) {
        this.invitationService = invitationService;
        this.groupService = groupService;
        this.memberService = memberService;
    }

    // Trang danh sách nhóm đã tham gia
    @GetMapping("/my-groups")
    public String myGroupsPage(HttpSession session, Model model) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            List<Group> userGroups = groupService.getUserGroups(userEmail);
            
            // Lấy thông tin chi tiết cho từng nhóm
            for (Group group : userGroups) {
                // Lấy danh sách thành viên của nhóm
                List<Member> members = memberService.getMembersByGroup(group.getId());
                group.setMembers(members);
                
                // Set member count
                group.setMemberCount(members != null ? members.size() : 0);
                
                // Đảm bảo có balance (nếu null thì set = 0)
                if (group.getBalance() == null) {
                    group.setBalance(BigDecimal.ZERO);
                }
                
                // Đảm bảo có trạng thái active
                if (group.isActive() == null) {
                    group.setActive(true);
                }
            }
            
            // Tính toán thống kê
            int activeGroupsCount = (int) userGroups.stream()
                .filter(g -> Boolean.TRUE.equals(g.isActive()))
                .count();
                
            BigDecimal totalBalance = userGroups.stream()
                .filter(g -> g.getBalance() != null)
                .map(Group::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
            int pendingCount = 0;
            try {
                pendingCount = invitationService.countPendingInvitationsForUser(userEmail);
            } catch (Exception e) {
                // Nếu lỗi khi lấy pending invitations, set = 0
                pendingCount = 0;
            }

            // Thêm vào model
            model.addAttribute("userGroups", userGroups != null ? userGroups : List.of());
            model.addAttribute("activeGroupsCount", activeGroupsCount);
            model.addAttribute("totalBalance", totalBalance);
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("userEmail", userEmail);

            return "user/groups/my-groups";
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để debug
            
            // Trả về dữ liệu rỗng thay vì lỗi
            model.addAttribute("userGroups", List.of());
            model.addAttribute("activeGroupsCount", 0);
            model.addAttribute("totalBalance", BigDecimal.ZERO);
            model.addAttribute("pendingCount", 0);
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("error", "Có lỗi khi tải danh sách nhóm: " + e.getMessage());
            
            return "user/groups/my-groups";
        }
    }

    // Trang join nhóm (mapping này phải đặt trước mapping /{groupId})
    @GetMapping("/join")
    public String joinGroupPage(HttpSession session, Model model) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            // Lấy danh sách lời mời chờ xác nhận
            List<Invitation> pendingInvitations = invitationService.getPendingInvitationsForUser(userEmail);
            model.addAttribute("pendingInvitations", pendingInvitations != null ? pendingInvitations : List.of());
            model.addAttribute("userEmail", userEmail);

            return "user/groups/join";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("pendingInvitations", List.of());
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("error", "Có lỗi khi tải lời mời: " + e.getMessage());
            return "user/groups/join";
        }
    }

    // Xem chi tiết nhóm
    @GetMapping("/{groupId}")
    public String viewGroup(@PathVariable Long groupId, HttpSession session, Model model) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            if (!memberService.isMemberOfGroup(groupId, userEmail)) {
                model.addAttribute("error", "Bạn không có quyền xem nhóm này");
                return "error";
            }

            Group group = groupService.findById(groupId);
            Map<String, Object> summary = groupService.getGroupSummary(groupId);
            List<Member> members = memberService.getMembersByGroup(groupId);

            model.addAttribute("group", group);
            model.addAttribute("summary", summary != null ? summary : Map.of());
            model.addAttribute("members", members != null ? members : List.of());
            model.addAttribute("userEmail", userEmail);

            return "user/groups/detail";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "error";
        }
    }

    // Rời khỏi nhóm
    @PostMapping("/{groupId}/leave")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> leaveGroup(
            @PathVariable Long groupId,
            HttpSession session) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return createErrorResponse("Phiên đăng nhập không hợp lệ");
        }

        try {
            memberService.leaveGroup(groupId, userEmail);
            return createResponse(true, "Đã rời khỏi nhóm thành công");
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // Utility methods
    private String getUserEmailFromSession(HttpSession session) {
        return (String) session.getAttribute("userEmail");
    }

    private ResponseEntity<Map<String, Object>> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}