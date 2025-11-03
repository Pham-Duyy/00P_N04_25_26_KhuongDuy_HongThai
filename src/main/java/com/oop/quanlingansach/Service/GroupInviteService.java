package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.GroupInvite;
import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Repository.GroupInviteRepository;
import com.oop.quanlingansach.Repository.GroupRepository;
import com.oop.quanlingansach.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class GroupInviteService {

    @Autowired
    private GroupInviteRepository groupInviteRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách lời mời đang chờ của user
    public List<GroupInvite> findPendingInvitesByUser(Long userId) {
        return groupInviteRepository.findByUser_IdAndStatus(userId, "PENDING");
    }

    // Chấp nhận lời mời
    public boolean acceptInvite(Long inviteId, Long userId) {
        GroupInvite invite = groupInviteRepository.findById(inviteId).orElse(null);
        if (invite == null || !invite.getUser().getId().equals(userId) || !"PENDING".equals(invite.getStatus())) {
            return false;
        }
        invite.setStatus("ACCEPTED");
        groupInviteRepository.save(invite);

        // Thêm user vào nhóm
        Group group = invite.getGroup();
        User user = invite.getUser();
        group.getMembers().add(user);
        groupRepository.save(group);

        return true;
    }

    public GroupInvite findById(Long id) {
        return groupInviteRepository.findById(id).orElse(null);
    }

    // Từ chối lời mời
    public void declineInvite(Long inviteId, Long userId) {
        GroupInvite invite = groupInviteRepository.findById(inviteId).orElse(null);
        if (invite != null && invite.getUser().getId().equals(userId) && "PENDING".equals(invite.getStatus())) {
            invite.setStatus("DECLINED");
            groupInviteRepository.save(invite);
        }
    }

    // Gửi lời mời tham gia nhóm (dùng cho admin)
    public void sendInvite(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (group != null && user != null) {
            GroupInvite invite = new GroupInvite();
            invite.setGroup(group);
            invite.setUser(user);
            invite.setStatus("PENDING");
            groupInviteRepository.save(invite);
        }
    }

    // Gửi lại lời mời tham gia nhóm (chỉ gửi nếu chưa là thành viên và chưa có lời mời PENDING)
    public boolean resendInvite(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (group == null || user == null) return false;

        // Kiểm tra nếu user đã là thành viên
        if (group.getMembers() != null && group.getMembers().stream().anyMatch(u -> u.getId().equals(userId))) {
            return false;
        }

        // Kiểm tra nếu đã có lời mời PENDING
        List<GroupInvite> existingInvites = groupInviteRepository.findByUser_IdAndGroup_IdAndStatus(userId, groupId, "PENDING");
        if (existingInvites != null && !existingInvites.isEmpty()) {
            return false;
        }

        // Gửi lại lời mời
        GroupInvite invite = new GroupInvite();
        invite.setGroup(group);
        invite.setUser(user);
        invite.setStatus("PENDING");
        groupInviteRepository.save(invite);
        return true;
    }

    // Lấy danh sách user chưa là thành viên và có thể gửi lại lời mời (chưa có lời mời PENDING)
    public List<User> findUsersCanResendInvite(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return new ArrayList<>();

        List<User> allUsers = userRepository.findAll();
        List<User> members = group.getMembers() != null ? group.getMembers() : new ArrayList<>();

        // Lấy user chưa là thành viên
        List<User> notMembers = allUsers.stream()
                .filter(u -> members.stream().noneMatch(m -> m.getId().equals(u.getId())))
                .collect(Collectors.toList());

        // Loại bỏ user đã có lời mời PENDING
        List<User> canResend = new ArrayList<>();
        for (User user : notMembers) {
            List<GroupInvite> pendingInvites = groupInviteRepository.findByUser_IdAndGroup_IdAndStatus(user.getId(), groupId, "PENDING");
            if (pendingInvites == null || pendingInvites.isEmpty()) {
                // Chỉ gửi lại cho user đã từng bị từ chối hoặc chưa từng được mời
                List<GroupInvite> declinedInvites = groupInviteRepository.findByUser_IdAndGroup_IdAndStatus(user.getId(), groupId, "DECLINED");
                if (declinedInvites != null && !declinedInvites.isEmpty()) {
                    canResend.add(user);
                } else if ((pendingInvites == null || pendingInvites.isEmpty()) && (declinedInvites == null || declinedInvites.isEmpty())) {
                    // Chưa từng được mời
                    canResend.add(user);
                }
            }
        }
        return canResend;
    }
}