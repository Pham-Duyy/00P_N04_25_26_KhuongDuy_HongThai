package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Member;

@RestController
@RequestMapping("/members")
public class MemberController {
    // Danh sách lưu trữ các member (giả lập database)
    private List<Member> members = new ArrayList<>();

    // Lấy tất cả member
    @GetMapping
    public List<Member> getAllMembers() {
        return members;
    }

    // Thêm mới member
    @PostMapping
    public Member addMember(@RequestBody Member member) {
        members.add(member);
        return member;
    }

    // Sửa member theo id
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        Member member = findMemberById(id);
        if (member != null) {
            member.setName(newMember.getName());
            member.setEmail(newMember.getEmail());
            member.setRole(newMember.getRole());
            return member;
        }
        // Không tìm thấy member
        return null;
    }

    // Xóa member theo id
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id) {
        Member member = findMemberById(id);
        if (member != null) {
            members.remove(member);
            return "Xóa thành công";
        }
        return "Không tìm thấy member";
    }

    // Hàm tiện ích tìm member theo id
    private Member findMemberById(Long id) {
        for (Member m : members) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
