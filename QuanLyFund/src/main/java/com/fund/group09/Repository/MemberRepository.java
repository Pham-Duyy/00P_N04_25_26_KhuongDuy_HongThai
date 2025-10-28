package com.fund.group09.Repository;

import com.fund.group09.Model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
boolean existsByGroupIdAndEmail(Long groupId, String email);

    // Tìm thành viên theo nhóm
    @Query("SELECT m FROM Member m WHERE m.group.id = :groupId")
    List<Member> findByGroupId(@Param("groupId") Long groupId);
    
    // Tìm thành viên theo nhóm và email của user
    @Query("SELECT m FROM Member m WHERE m.group.id = :groupId AND m.user.email = :email")
    Optional<Member> findByGroupIdAndUserEmail(@Param("groupId") Long groupId, @Param("email") String email);
    
    // Đếm số thành viên trong nhóm
    @Query("SELECT COUNT(m) FROM Member m WHERE m.group.id = :groupId")
    long countByGroupId(@Param("groupId") Long groupId);
    
    // Kiểm tra thành viên tồn tại trong nhóm
    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE m.group.id = :groupId AND m.user.email = :email")
    boolean existsByGroupIdAndUserEmail(@Param("groupId") Long groupId, @Param("email") String email);
    
    // Tìm thành viên theo nhóm và vai trò
    @Query("SELECT m FROM Member m WHERE m.group.id = :groupId AND m.role = :role")
    List<Member> findByGroupIdAndRole(@Param("groupId") Long groupId, @Param("role") String role);
    
    // Tìm admin của nhóm
    @Query("SELECT m FROM Member m WHERE m.group.id = :groupId AND m.role = 'ADMIN'")
    Optional<Member> findAdminByGroupId(@Param("groupId") Long groupId);
    
    // Tìm thành viên theo email của user
    @Query("SELECT m FROM Member m WHERE m.user.email = :email")
    List<Member> findByUserEmail(@Param("email") String email);
    
    // Xóa tất cả thành viên của nhóm
    @Query("DELETE FROM Member m WHERE m.group.id = :groupId")
    void deleteByGroupId(@Param("groupId") Long groupId);
    
    // Tìm thành viên theo nhóm và email của user (không phân biệt hoa/thường)
    @Query("SELECT m FROM Member m WHERE m.group.id = :groupId AND LOWER(m.user.email) = LOWER(:email)")
    Optional<Member> findByGroupIdAndUserEmailIgnoreCase(@Param("groupId") Long groupId, @Param("email") String email);
}