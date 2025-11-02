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
}