package com.fund.group09.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.group09.Model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}