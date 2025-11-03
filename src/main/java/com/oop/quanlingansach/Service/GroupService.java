package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.User;
import java.util.List;

/**
 * Service cho chức năng quản lý nhóm.
 */
public interface GroupService {
    /**
     * Lấy tất cả nhóm.
     */
    List<Group> findAll();

    /**
     * Tìm kiếm nhóm theo tên.
     */
    List<Group> searchByName(String keyword);

    /**
     * Tìm nhóm theo ID.
     */
    Group findById(Long id);

    /**
     * Tạo nhóm mới với danh sách thành viên.
     * @param group Nhóm mới
     * @param memberIds Danh sách ID thành viên
     */
    void createGroupWithMembers(Group group, List<Long> memberIds);

    /**
     * Cập nhật thông tin nhóm và thành viên.
     * @param groupId ID nhóm
     * @param group Thông tin nhóm mới
     * @param memberIds Danh sách ID thành viên mới
     */
    void updateGroup(Long groupId, Group group, List<Long> memberIds);

    /**
     * Xóa nhóm theo ID.
     */
    void deleteGroup(Long groupId);

    /**
     * Lấy danh sách thành viên của nhóm.
     */
    List<User> getGroupMembers(Long groupId);

    /**
     * Gửi thông báo mời tham gia nhóm cho các user.
     */
    void notifyUsersForGroupInvite(Long groupId, List<Long> memberIds);

    /**
     * User xác nhận tham gia nhóm.
     */
    void confirmJoinGroup(Long groupId, Long userId);

    /**
     * Thêm nhóm mới (không cần thành viên).
     */
    void addGroup(Group group);

    /**
     * Cập nhật thông tin nhóm (không cập nhật thành viên).
     */
    void updateGroup(Long id, Group group);

    /**
     * Lấy trạng thái xác nhận của từng thành viên trong nhóm.
     */
    List<String> getMemberConfirmStatus(Long groupId);

    /**
     * Lấy danh sách nhóm mà user đã tham gia (tương thích cho controller).
     */
    List<Group> findGroupsByMember(Long userId);

    /**
     * Lấy danh sách nhóm mà user đã tham gia (tên rõ ràng, dùng cho controller mới).
     */
    List<Group> findGroupsByMemberId(Long userId);

    /**
     * Xóa thành viên khỏi nhóm.
     */
    void removeMemberFromGroup(Long groupId, Long userId);
}