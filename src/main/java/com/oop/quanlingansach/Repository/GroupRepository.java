package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    // Tìm nhóm theo tên (tìm kiếm gần đúng, không phân biệt hoa thường)
    List<Group> findByNameContainingIgnoreCase(String keyword);

    // Tìm nhóm theo tên chính xác
    Optional<Group> findByName(String name);

    // Tìm nhóm theo loại
    List<Group> findByType(String type);

    // Tìm nhóm theo ID admin tạo nhóm
    List<Group> findByAdminId(Long adminId);

    // Kiểm tra nhóm có tồn tại theo tên không
    boolean existsByName(String name);

    // Tìm nhóm mà user có tham gia (dùng cho findGroupsByMember)
    @Query("SELECT g FROM Group g JOIN g.members m WHERE m.id = :userId")
    List<Group> findByMembersId(@Param("userId") Long userId);

    // Đếm số lượng thành viên trong nhóm
    @Query("SELECT COUNT(m) FROM Group g JOIN g.members m WHERE g.id = :groupId")
    Long countMembersByGroupId(@Param("groupId") Long groupId);

    // Tìm nhóm theo từ khóa (tên hoặc mô tả)
    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Group> searchByKeyword(@Param("keyword") String keyword);
}