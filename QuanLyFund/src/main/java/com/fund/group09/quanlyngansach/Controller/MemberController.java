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

import com.fund.group09.quanlyngansach.Model.Member;
import com.fund.group09.quanlyngansach.Repository.MemberRepository;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*") // Cho phép gọi từ frontend (React, Angular,...)
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    // ✅ Lấy tất cả member
    @GetMapping
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ✅ Lấy member theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm mới member
    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        Member saved = memberRepository.save(member);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Cập nhật member
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setName(newMember.getName());
                    member.setEmail(newMember.getEmail());
                    member.setRole(newMember.getRole());
                    Member updated = memberRepository.save(member);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Xóa member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (!memberRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        memberRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
