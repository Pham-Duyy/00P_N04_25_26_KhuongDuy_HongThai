package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Role;

@RestController
@RequestMapping("/roles")
public class RoleController {
    // Danh sách lưu trữ các role (giả lập database)
    private List<Role> roles = new ArrayList<>();

    // Lấy tất cả role
    @GetMapping
    public List<Role> getAllRoles() {
        return roles;
    }

    // Thêm mới role
    @PostMapping
    public Role addRole(@RequestBody Role role) {
        roles.add(role);
        return role;
    }

    // Sửa role theo id
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role newRole) {
        Role role = findRoleById(id);
        if (role != null) {
            role.setName(newRole.getName());
            role.setDescription(newRole.getDescription());
            return role;
        }
        // Không tìm thấy role
        return null;
    }

    // Xóa role theo id
    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        Role role = findRoleById(id);
        if (role != null) {
            roles.remove(role);
            return "Xóa thành công";
        }
        return "Không tìm thấy role";
    }

    // Hàm tiện ích tìm role theo id
    private Role findRoleById(Long id) {
        for (Role r : roles) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }
}
