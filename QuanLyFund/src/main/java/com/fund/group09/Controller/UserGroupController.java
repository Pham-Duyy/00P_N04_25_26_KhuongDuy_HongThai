package com.fund.group09.Controller;

import com.fund.group09.Model.*;
import com.fund.group09.Service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

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

    // Trang danh sách nhóm và lời mời
    @GetMapping
    public String groupsPage(HttpSession session, Model model) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return "redirect:/login";
        }

        try {
            List<Group> myGroups = groupService.getUserGroups(userEmail);
            model.addAttribute("myGroups", myGroups);

            // Không lấy lời mời ở đây, chỉ lấy ở trang /join
            List<Group> newGroups = groupService.getNewGroupsForUser(userEmail);
            model.addAttribute("newGroups", newGroups);

            Map<Long, Map<String, Object>> groupSummaries = new HashMap<>();
            for (Group group : myGroups) {
                groupSummaries.put(group.getId(), groupService.getGroupSummary(group.getId()));
            }
            model.addAttribute("groupSummaries", groupSummaries);

            return "user/groups/index";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "error";
        }
    }

    // Trang hiển thị lời mời tham gia nhóm
    @GetMapping("/join")
    public String joinGroupPage(HttpSession session, Model model) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return "redirect:/login";
        }
        List<Invitation> pendingInvitations = invitationService.getPendingInvitationsForUser(userEmail);
        model.addAttribute("pendingInvitations", pendingInvitations);
        model.addAttribute("userEmail", userEmail);
        return "user/groups/join";
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
            model.addAttribute("summary", summary);
            model.addAttribute("members", members);

            return "user/groups/detail";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "error";
        }
    }

    // Xử lý chấp nhận lời mời
    @PostMapping("/invitations/{invitationId}/accept")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> acceptInvitation(
            @PathVariable Long invitationId,
            HttpSession session) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return createErrorResponse("Phiên đăng nhập không hợp lệ");
        }

        try {
            Member newMember = memberService.acceptInvitation(invitationId, userEmail);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đã tham gia nhóm thành công!");
            response.put("memberId", newMember.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
        }
    }

    // Xử lý từ chối lời mời
    @PostMapping("/invitations/{invitationId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectInvitation(
            @PathVariable Long invitationId,
            HttpSession session) {
        String userEmail = getUserEmailFromSession(session);
        if (userEmail == null) {
            return createErrorResponse("Phiên đăng nhập không hợp lệ");
        }

        try {
            memberService.rejectInvitation(invitationId, userEmail);
            return createResponse(true, "Đã từ chối lời mời thành công");
        } catch (Exception e) {
            return createErrorResponse(e.getMessage());
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