package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh tại đây nếu cần
}