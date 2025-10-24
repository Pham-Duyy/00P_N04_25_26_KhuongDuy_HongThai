package com.fund.group09.Controller;

import com.fund.group09.Model.Member;
import com.fund.group09.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //  Lấy tất cả
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    //  Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  Thêm mới
    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        Member saved = memberService.addMember(member);
        return ResponseEntity.status(201).body(saved);
    }

    //  Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        return memberService.updateMember(id, newMember)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //  Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        boolean deleted = memberService.deleteMember(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //  Lấy theo nhóm
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Member>> getMembersByGroup(@PathVariable Long groupId) {
        List<Member> members = memberService.getMembersByGroup(groupId);
        if (members.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(members);
    }

    //  Tổng kết tài chính cá nhân
    @GetMapping("/{id}/summary")
    public ResponseEntity<Map<String, Object>> getMemberSummary(@PathVariable Long id) {
        Map<String, Object> summary = memberService.getMemberSummary(id);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }
}
