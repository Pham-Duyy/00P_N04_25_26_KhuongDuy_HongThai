package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Repository.GroupRepository;
import com.oop.quanlingansach.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> findGroupsByMember(Long userId) {
        return groupRepository.findByMembersId(userId);
    }

    @Override
    public List<Group> searchByName(String keyword) {
        return groupRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    @Override
    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    @Override
    public void updateGroup(Long groupId, Group group) {
        Group existing = groupRepository.findById(groupId).orElse(null);
        if (existing != null) {
            existing.setName(group.getName());
            existing.setDescription(group.getDescription());
            existing.setType(group.getType());
            existing.setFundAmount(group.getFundAmount());
            existing.setTargetAmount(group.getTargetAmount());
            existing.setActive(group.isActive());
            groupRepository.save(existing);
        }
    }

    @Override
    public void createGroupWithMembers(Group group, List<Long> memberIds) {
        List<User> members = userRepository.findAllById(memberIds);
        group.setMembers(members);
        groupRepository.save(group);
    }

    @Override
    public void updateGroup(Long groupId, Group group, List<Long> memberIds) {
        Group existing = groupRepository.findById(groupId).orElse(null);
        if (existing != null) {
            existing.setName(group.getName());
            existing.setDescription(group.getDescription());
            existing.setType(group.getType());
            existing.setMembers(userRepository.findAllById(memberIds));
            groupRepository.save(existing);
        }
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public List<User> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        return group != null ? group.getMembers() : new ArrayList<>();
    }

    @Override
    public void notifyUsersForGroupInvite(Long groupId, List<Long> memberIds) {
        // TODO: Gửi thông báo cho user (tùy vào thiết kế Notification)
    }

    @Override
    public void confirmJoinGroup(Long groupId, Long userId) {
        // TODO: Xử lý xác nhận tham gia nhóm
    }

    @Override
    public List<String> getMemberConfirmStatus(Long groupId) {
        // TODO: Trả về trạng thái xác nhận của từng thành viên
        return new ArrayList<>();
    }

    @Override
    public void removeMemberFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group != null && group.getMembers() != null) {
            group.getMembers().removeIf(user -> user.getId().equals(userId));
            groupRepository.save(group);
        }
    }
}