package com.fund.group09.Repository;

import com.fund.group09.Model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.fund")
    List<Group> findAllWithFunds();
    
    // Tìm kiếm nhóm theo tên hoặc mô tả (phân trang, không phân biệt hoa thường)
    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "OR LOWER(g.description) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Page<Group> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        @Param("keyword") String keyword,
        @Param("keyword") String sameKeyword,
        Pageable pageable
    );

    // Nhóm chưa có user tham gia
    @Query("SELECT g FROM Group g WHERE g.id NOT IN (SELECT m.group.id FROM Member m WHERE m.user.id = :userId)")
    List<Group> findAvailableGroupsForUser(@Param("userId") Long userId);

    // Nhóm mới do admin tạo mà user chưa tham gia
    @Query("SELECT g FROM Group g WHERE g.createdBy = 'ADMIN' AND g.id NOT IN (SELECT m.group.id FROM Member m WHERE m.user.id = :userId)")
    List<Group> findNewGroupsByAdmin(@Param("userId") Long userId);

    // Tìm nhóm theo người tạo
    List<Group> findByCreatedBy(String createdBy);

    // Lấy tất cả nhóm đang hoạt động
    @Query("SELECT g FROM Group g WHERE g.isActive = true")
    List<Group> findActiveGroups();

    // Đếm số thành viên của nhóm
    @Query("SELECT COUNT(m) FROM Member m WHERE m.group.id = :groupId")
    long countMembers(@Param("groupId") Long groupId);

    // Lấy danh sách nhóm mà user là thành viên
    @Query("SELECT g FROM Group g JOIN Member m ON m.group.id = g.id WHERE m.user.id = :userId")
    List<Group> findGroupsByMemberId(@Param("userId") Long userId);
}