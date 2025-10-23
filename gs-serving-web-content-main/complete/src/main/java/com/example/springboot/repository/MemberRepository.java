package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Member;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private List<Member> members = new ArrayList<>();

    public List<Member> findAll() {
        return members;
    }

    public void save(Member member) {
        members.add(member);
    }

    public Member findById(Long id) {
        for (Member m : members) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public boolean update(Long id, Member newMember) {
        Member member = findById(id);
        if (member != null) {
            member.setName(newMember.getName());
            member.setEmail(newMember.getEmail());
            member.setRole(newMember.getRole());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Member member = findById(id);
        if (member != null) {
            members.remove(member);
            return true;
        }
        return false;
    }
}
