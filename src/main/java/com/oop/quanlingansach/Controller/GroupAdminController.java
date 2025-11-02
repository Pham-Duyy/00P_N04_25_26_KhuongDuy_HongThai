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
    // ...existing code...

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
}