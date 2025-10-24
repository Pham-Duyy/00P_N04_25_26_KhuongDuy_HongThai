package com.fund.group09.quanlyngansach.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fund.group09.quanlyngansach.Model.Group;
import com.fund.group09.quanlyngansach.Repository.GroupRepository;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    // ✅ Lấy tất cả group
    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    // ✅ Lấy group theo id
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm group mới
    @PostMapping
    public ResponseEntity<Group> addGroup(@RequestBody Group group) {
        Group saved = groupRepository.save(group);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Cập nhật group theo id
    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group newGroup) {
        return groupRepository.findById(id)
                .map(group -> {
                    group.setName(newGroup.getName());
                    group.setDescription(newGroup.getDescription());
                    group.setMembers(newGroup.getMembers());
                    group.setFund(newGroup.getFund());
                    Group updated = groupRepository.save(group);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Xóa group theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        if (!groupRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        groupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
