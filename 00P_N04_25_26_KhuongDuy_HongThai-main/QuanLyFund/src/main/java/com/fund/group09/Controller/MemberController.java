package com.fund.group09.Controller;

import com.fund.group09.Model.Member;
import com.fund.group09.Model.Invitation;
import com.fund.group09.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // Lấy tất cả thành viên (chỉ admin)
    @GetMapping
    public ResponseEntity<?> getAllMembers(HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Chỉ admin mới có quyền xem danh sách thành viên");
        }
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // Lấy thông tin thành viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAuthorized(id, session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Không có quyền truy cập thông tin này");
        }
        
        try {
            Member member = memberService.getMemberById(id);
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }

    // Xem danh sách lời mời tham gia nhóm
    @GetMapping("/invitations")
    public ResponseEntity<?> getPendingInvitations(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Chưa đăng nhập");
        }

        List<Invitation> invitations = memberService.getPendingInvitations(userEmail);
        return ResponseEntity.ok(invitations);
    }

    // Chấp nhận lời mời tham gia nhóm
    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<?> acceptInvitation(
            @PathVariable Long invitationId,
            HttpSession session
    ) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Chưa đăng nhập");
        }

        try {
            Member member = memberService.acceptInvitation(invitationId, userEmail);
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Từ chối lời mời tham gia nhóm
    @PostMapping("/invitations/{invitationId}/reject")
    public ResponseEntity<?> rejectInvitation(
            @PathVariable Long invitationId,
            HttpSession session
    ) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Chưa đăng nhập");
        }

        try {
            memberService.rejectInvitation(invitationId, userEmail);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Rời khỏi nhóm
    @PostMapping("/groups/{groupId}/leave")
    public ResponseEntity<?> leaveGroup(
            @PathVariable Long groupId,
            HttpSession session
    ) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Chưa đăng nhập");
        }

        try {
            memberService.leaveGroup(groupId, userEmail);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Lấy danh sách thành viên theo nhóm
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<?> getMembersByGroup(
            @PathVariable Long groupId,
            HttpSession session
    ) {
        if (!isMemberOfGroup(groupId, session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Không có quyền xem thông tin nhóm này");
        }

        List<Member> members = memberService.getMembersByGroup(groupId);
        return ResponseEntity.ok(members);
    }

    // Xem tổng kết tài chính cá nhân
    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getMemberSummary(
            @PathVariable Long id,
            HttpSession session
    ) {
        if (!isAuthorized(id, session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Không có quyền xem thông tin này");
        }

        Map<String, Object> summary = memberService.getMemberSummary(id);
        return ResponseEntity.ok(summary);
    }

    // Helper methods for authorization
    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    private boolean isAuthorized(Long memberId, HttpSession session) {
        if (isAdmin(session)) return true;
        Long userId = (Long) session.getAttribute("userId");
        return userId != null && userId.equals(memberId);
    }

    private boolean isMemberOfGroup(Long groupId, HttpSession session) {
        if (isAdmin(session)) return true;
        String userEmail = (String) session.getAttribute("userEmail");
        return memberService.isMemberOfGroup(groupId, userEmail);
    }
}