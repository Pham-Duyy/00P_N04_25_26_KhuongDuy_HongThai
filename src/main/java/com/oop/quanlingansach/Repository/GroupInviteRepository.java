package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.GroupInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupInviteRepository extends JpaRepository<GroupInvite, Long> {
    
    // Tìm lời mời theo user ID và status
    List<GroupInvite> findByUser_IdAndStatus(Long userId, String status);
    
    // Tìm lời mời theo group ID và user ID
    Optional<GroupInvite> findByGroup_IdAndUser_Id(Long groupId, Long userId);
    
    // Tìm tất cả lời mời của một user
    List<GroupInvite> findByUser_Id(Long userId);
    
    // Tìm tất cả lời mời của một group
    List<GroupInvite> findByGroup_Id(Long groupId);
    
    // Tìm lời mời theo status
    List<GroupInvite> findByStatus(String status);
    
    // Query tùy chỉnh để tìm lời mời pending của user với thông tin group
    @Query("SELECT gi FROM GroupInvite gi JOIN FETCH gi.group WHERE gi.user.id = :userId AND gi.status = :status")
    List<GroupInvite> findPendingInvitesWithGroup(@Param("userId") Long userId, @Param("status") String status);
    
    // Đếm số lời mời pending của user
    @Query("SELECT COUNT(gi) FROM GroupInvite gi WHERE gi.user.id = :userId AND gi.status = 'PENDING'")
    Long countPendingInvitesByUser(@Param("userId") Long userId);
    
    // Kiểm tra xem user đã có lời mời từ group này chưa
    boolean existsByGroup_IdAndUser_Id(Long groupId, Long userId);
}