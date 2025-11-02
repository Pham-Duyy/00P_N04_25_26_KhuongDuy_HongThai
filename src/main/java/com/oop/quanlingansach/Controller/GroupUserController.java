package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.UserService;
import com.oop.quanlingansach.Service.GroupInviteService;
import com.oop.quanlingansach.Model.GroupInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/user/groups")
public class GroupUserController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupInviteService groupInviteService;

    // Hiển thị danh sách lời mời tham gia nhóm của user
    @GetMapping("/invites")
    public String viewInvites(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        List<GroupInvite> invites = groupInviteService.findPendingInvitesByUser(user.getId());
        model.addAttribute("invites", invites);
        return "user/groups/invites";
    }

    // Xem chi tiết nhóm từ lời mời
    @GetMapping("/invites/{inviteId}/group")
    public String viewGroupFromInvite(@PathVariable Long inviteId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        GroupInvite invite = groupInviteService.findById(inviteId);
        if (invite == null || !invite.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xem nhóm này!");
            return "redirect:/user/groups/invites";
        }
        Group group = invite.getGroup();
        model.addAttribute("group", group);
        return "user/groups/group-detail";
    }

    // Hiển thị danh sách nhóm mà user đã tham gia (trả về trang my-groups)
    @GetMapping
    public String listMyGroups(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        List<Group> myGroups = groupService.findGroupsByMember(user.getId());
        int adminCount = 0;
        int memberCount = 0;
        if (myGroups != null) {
            for (Group g : myGroups) {
                if (g != null && g.getAdminId() != null && g.getAdminId().equals(user.getId())) {
                    adminCount++;
                } else if (g != null) {
                    memberCount++;
                }
            }
        }
        model.addAttribute("groups", myGroups);
        model.addAttribute("currentUser", user);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("memberCount", memberCount);
        return "user/groups/my-groups";
    }

    // Xem chi tiết nhóm đã tham gia
    @GetMapping("/{groupId}")
public String viewJoinedGroup(@PathVariable Long groupId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
        redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
        return "redirect:/login";
    }
    Group group = groupService.findById(groupId);
    if (group == null) {
        redirectAttributes.addFlashAttribute("error", "Nhóm không tồn tại!");
        return "redirect:/user/groups";
    }
    // Kiểm tra user có phải thành viên nhóm không
    if (group.getMembers() == null || group.getMembers().stream().noneMatch(m -> m.getId().equals(user.getId()))) {
        redirectAttributes.addFlashAttribute("error", "Bạn không phải thành viên nhóm này!");
        return "redirect:/user/groups";
    }
    model.addAttribute("group", group);
    model.addAttribute("currentUser", user);
    model.addAttribute("members", group.getMembers()); // Truyền danh sách thành viên cho view
    return "user/groups/group-detail";
}

    // User rời khỏi nhóm
    @PostMapping("/{groupId}/leave")
    public String leaveGroup(@PathVariable Long groupId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        Group group = groupService.findById(groupId);
        if (group == null) {
            redirectAttributes.addFlashAttribute("error", "Nhóm không tồn tại!");
            return "redirect:/user/groups";
        }
        // Không cho phép admin rời khỏi nhóm
        if (group.getAdminId() != null && group.getAdminId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("error", "Admin không thể rời khỏi nhóm!");
            return "redirect:/user/groups/" + groupId;
        }
        groupService.removeMemberFromGroup(groupId, user.getId());
        redirectAttributes.addFlashAttribute("success", "Bạn đã rời khỏi nhóm thành công!");
        return "redirect:/user/groups";
    }

    // User chấp nhận lời mời tham gia nhóm
    @PostMapping("/invites/{inviteId}/accept")
    public String acceptInvite(@PathVariable Long inviteId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        boolean success = groupInviteService.acceptInvite(inviteId, user.getId());
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Bạn đã tham gia nhóm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Lời mời không hợp lệ hoặc đã hết hạn.");
        }
        return "redirect:/user/groups/invites";
    }

    // User từ chối lời mời tham gia nhóm
    @PostMapping("/invites/{inviteId}/decline")
    public String declineInvite(@PathVariable Long inviteId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để truy cập trang này!");
            return "redirect:/login";
        }
        groupInviteService.declineInvite(inviteId, user.getId());
        redirectAttributes.addFlashAttribute("success", "Bạn đã từ chối lời mời tham gia nhóm.");
        return "redirect:/user/groups/invites";
    }
}