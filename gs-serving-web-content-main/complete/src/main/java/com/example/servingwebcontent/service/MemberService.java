package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Member;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private List<Member> members = new ArrayList<>();

    public List<Member> getAllMembers() {
        return members;
    }

    public Member addMember(Member member) {
        members.add(member);
        return member;
    }

    public Member updateMember(Long id, Member newMember) {
        for (Member member : members) {
            if (member.getId().equals(id)) {
                member.setName(newMember.getName());
                member.setEmail(newMember.getEmail());
                member.setRole(newMember.getRole());
                return member;
            }
        }
        return null;
    }

    public boolean deleteMember(Long id) {
        return members.removeIf(m -> m.getId().equals(id));
    }

    public Member findMemberById(Long id) {
        for (Member m : members) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
