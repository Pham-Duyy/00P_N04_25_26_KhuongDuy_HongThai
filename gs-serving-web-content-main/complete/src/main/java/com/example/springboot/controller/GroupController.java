package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Group;

@RestController
@RequestMapping("/groups")
public class GroupController {
    // Danh sách lưu trữ các group (giả lập database)
    private List<Group> groups = new ArrayList<>();

    // Lấy tất cả group
    @GetMapping
    public List<Group> getAllGroups() {
        return groups;
    }

    // Thêm mới group
    @PostMapping
    public Group addGroup(@RequestBody Group group) {
        groups.add(group);
        return group;
    }

    // Sửa group theo id
    @PutMapping("/{id}")
    public Group updateGroup(@PathVariable Long id, @RequestBody Group newGroup) {
        Group group = findGroupById(id);
        if (group != null) {
            group.setName(newGroup.getName());
            group.setDescription(newGroup.getDescription());
            group.setMembers(newGroup.getMembers());
            group.setFund(newGroup.getFund());
            return group;
        }
        // Không tìm thấy group
        return null;
    }

    // Xóa group theo id
    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Long id) {
        Group group = findGroupById(id);
        if (group != null) {
            groups.remove(group);
            return "Xóa thành công";
        }
        return "Không tìm thấy group";
    }

    // Hàm tiện ích tìm group theo id
    private Group findGroupById(Long id) {
        for (Group g : groups) {
            if (g.getId().equals(id)) {
                return g;
            }
        }
        return null;
    }
}
