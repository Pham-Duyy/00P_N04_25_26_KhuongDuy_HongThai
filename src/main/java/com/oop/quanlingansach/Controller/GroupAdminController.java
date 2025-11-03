package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.UserService;
import com.oop.quanlingansach.Service.GroupInviteService;
import com.oop.quanlingansach.Model.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/groups")
public class GroupAdminController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupInviteService groupInviteService;

    // Hiển thị danh sách nhóm + form tạo nhóm (trên cùng 1 trang)
    @GetMapping
    public String listGroups(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Group> groups = (keyword == null || keyword.isEmpty())
                ? groupService.findAll()
                : groupService.searchByName(keyword);
        model.addAttribute("groups", groups);
        model.addAttribute("keyword", keyword);
        model.addAttribute("group", new Group()); // Để form tạo nhóm dùng object rỗng

        // Lấy danh sách user để chọn thành viên khi tạo nhóm
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "admin/groups/group-create";
    }

    // Xử lý tạo nhóm và gửi lời mời cho các thành viên được chọn
    @PostMapping("/create")
    public String createGroup(@ModelAttribute Group group,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để tạo nhóm!");
            return "redirect:/login";
        }
        group.setAdminId(user.getId());
        groupService.addGroup(group);

        // Gửi lời mời cho tất cả user có vai trò USER (trừ admin)
        List<User> allUsers = userService.findAllUsers();
        for (User u : allUsers) {
            if (!u.getId().equals(user.getId()) && u.getRole() == User.Role.USER) {
                groupInviteService.sendInvite(group.getId(), u.getId());
            }
        }

        redirectAttributes.addFlashAttribute("success", "Tạo nhóm thành công! Đã gửi lời mời cho tất cả thành viên.");
        return "redirect:/admin/groups";
    }

    // Gửi lại lời mời tham gia nhóm cho user chưa tham gia
    // ...existing code...
@PostMapping("/{groupId}/invite-user")
public String inviteUserToGroup(
        @PathVariable Long groupId,
        @RequestParam("userId") Long userId,
        @RequestParam(value = "message", required = false) String message,
        RedirectAttributes redirectAttributes,
        HttpSession session) {

    // Chỉ cần tài khoản có vai trò ADMIN là được gửi
    User admin = (User) session.getAttribute("user");
    if (admin == null || admin.getRole() != User.Role.ADMIN) {
        redirectAttributes.addFlashAttribute("error", "Bạn không có quyền gửi yêu cầu cho nhóm này!");
        return "redirect:/admin/groups";
    }

    Group group = groupService.findById(groupId);
    if (group == null) {
        redirectAttributes.addFlashAttribute("error", "Nhóm không tồn tại!");
        return "redirect:/admin/groups";
    }

    // Gửi lời mời
    boolean sent = groupInviteService.resendInvite(groupId, userId);
    if (sent) {
        redirectAttributes.addFlashAttribute("success", "Đã gửi yêu cầu tham gia nhóm!");
    } else {
        redirectAttributes.addFlashAttribute("error", "Không thể gửi yêu cầu (user đã là thành viên hoặc đã có lời mời).");
    }
    return "redirect:/admin/groups";
}
// ...existing code...

    // Xử lý cập nhật nhóm (chỉ cập nhật thông tin nhóm, không cập nhật thành viên ở đây)
    @PostMapping("/{id}/edit")
    public String updateGroup(@PathVariable Long id,
                              @ModelAttribute Group group,
                              RedirectAttributes redirectAttributes) {
        groupService.updateGroup(id, group); // Chỉ cập nhật thông tin nhóm
        redirectAttributes.addFlashAttribute("success", "Cập nhật nhóm thành công!");
        return "redirect:/admin/groups";
    }

    // API trả về dữ liệu nhóm dạng JSON để load vào form sửa (AJAX)
    @GetMapping("/{id}/json")
    @ResponseBody
    public Group getGroupJson(@PathVariable Long id) {
        return groupService.findById(id);
    }

    // Xem chi tiết nhóm (chuyển sang trang riêng)
    @GetMapping("/{id}")
    public String viewGroup(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id);
        model.addAttribute("group", group);
        return "admin/groups/group-detail";
    }

    // Xóa nhóm
    @PostMapping("/{id}/delete")
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        groupService.deleteGroup(id);
        redirectAttributes.addFlashAttribute("success", "Đã xóa nhóm!");
        return "redirect:/admin/groups";
    }
    // Xóa thành viên khỏi nhóm
@PostMapping("/{groupId}/members/{userId}/remove")
public String removeMemberFromGroup(
        @PathVariable Long groupId,
        @PathVariable Long userId,
        RedirectAttributes redirectAttributes,
        HttpSession session) {

    // Chỉ cần tài khoản có vai trò ADMIN là được xóa
    User admin = (User) session.getAttribute("user");
    if (admin == null || admin.getRole() != User.Role.ADMIN) {
        redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xóa thành viên khỏi nhóm này!");
        return "redirect:/admin/groups/" + groupId;
    }

    groupService.removeMemberFromGroup(groupId, userId);
    redirectAttributes.addFlashAttribute("success", "Đã xóa thành viên khỏi nhóm!");
    return "redirect:/admin/groups/" + groupId;
}
}