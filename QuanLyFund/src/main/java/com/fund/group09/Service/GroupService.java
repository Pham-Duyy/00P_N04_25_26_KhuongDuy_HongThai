package com.fund.group09.Service;

import com.fund.group09.Model.*;
import com.fund.group09.Repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final InvitationRepository invitationRepository;
    private final MemberRepository memberRepository;

    // Constants
    private static final int DEFAULT_MAX_MEMBERS = 50;
    private static final String DEFAULT_GROUP_TYPE = "PUBLIC";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_ACCEPTED = "ACCEPTED";
    private static final String STATUS_REJECTED = "REJECTED";

    public GroupService(GroupRepository groupRepository,
                       UserRepository userRepository,
                       ExpenseRepository expenseRepository,
                       IncomeRepository incomeRepository,
                       InvitationRepository invitationRepository,
                       MemberRepository memberRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.invitationRepository = invitationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Group createGroup(String name, String description, String adminEmail) {
        User admin = findUserByEmail(adminEmail);

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedDate(LocalDateTime.now());
        group.setIsActive(true);
        group.setJoinCode(generateJoinCode());
        group.setMaxMembers(DEFAULT_MAX_MEMBERS);
        group.setGroupType(DEFAULT_GROUP_TYPE);
        group.setCreatedBy(adminEmail);
        
        group = groupRepository.save(group);

        // Add admin as first member
        Member adminMember = new Member();
        adminMember.setUser(admin);
        adminMember.setGroup(group);
        adminMember.setRole("ADMIN"); // Set role cho admin
        adminMember.setJoinedAt(group.getCreatedDate()); // Ngày tham gia của admin là ngày tạo nhóm
        memberRepository.save(adminMember);

        // Create invitations for other users
        createInvitationsForNewGroup(group, adminEmail);

        return group;
    }

    @Transactional
    public boolean acceptInvitation(Long invitationId, String userEmail) {
        Invitation invitation = findAndValidateInvitation(invitationId, userEmail);
        Group group = invitation.getGroup();
        User user = invitation.getUser();

        validateGroupCapacity(group);

        // Create new member
        Member newMember = new Member();
        newMember.setUser(user);
        newMember.setGroup(group);
        newMember.setRole("MEMBER"); // Set role cho member
        LocalDateTime now = LocalDateTime.now();
        newMember.setJoinedAt(now); // Ngày tham gia là thời điểm xác nhận
        System.out.println("DEBUG: GroupService - Setting joinedAt to " + now); // Thêm log debug
        memberRepository.save(newMember);
        System.out.println("DEBUG: GroupService - Saved member with joinedAt: " + newMember.getJoinedAt()); // Thêm log debug

        // Update invitation status
        updateInvitationStatus(invitation, STATUS_ACCEPTED);

        return true;
    }
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Transactional
    public boolean rejectInvitation(Long invitationId, String userEmail) {
        Invitation invitation = findAndValidateInvitation(invitationId, userEmail);
        updateInvitationStatus(invitation, STATUS_REJECTED);
        return true;
    }

     @Transactional(readOnly = true)
    public Map<String, Object> getGroupSummary(Long groupId) {
        Group group = findById(groupId);
        Double totalExpense = expenseRepository.sumAmountByGroupId(groupId);
        Double totalIncome = incomeRepository.sumAmountByGroupId(groupId); // Sửa lỗi: dùng incomeRepository thay vì expenseRepository

        return createGroupSummary(group, totalExpense, totalIncome);
    }

    // Helper methods
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }

    private void createInvitationsForNewGroup(Group group, String adminEmail) {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (!user.getEmail().equals(adminEmail)) {
                createInvitation(user, group, adminEmail);
            }
        }
    }

    private void createInvitation(User user, Group group, String invitedBy) {
        Invitation invitation = new Invitation();
        invitation.setUser(user);
        invitation.setGroup(group);
        invitation.setStatus(STATUS_PENDING);
        invitation.setInvitedBy(invitedBy);
        invitation.setInvitedDate(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    private Invitation findAndValidateInvitation(Long invitationId, String userEmail) {
        Invitation invitation = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời"));

        if (!invitation.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Không có quyền với lời mời này");
        }

        if (!STATUS_PENDING.equals(invitation.getStatus())) {
            throw new RuntimeException("Lời mời không còn hiệu lực");
        }

        return invitation;
    }

    private void validateGroupCapacity(Group group) {
        if (group.getMembers().size() >= group.getMaxMembers()) {
            throw new RuntimeException("Nhóm đã đạt số lượng thành viên tối đa");
        }
    }

    private void updateInvitationStatus(Invitation invitation, String status) {
        invitation.setStatus(status);
        invitation.setResponseDate(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    private Map<String, Object> createGroupSummary(Group group, Double totalExpense, Double totalIncome) {
        totalExpense = totalExpense != null ? totalExpense : 0.0;
        totalIncome = totalIncome != null ? totalIncome : 0.0;

        Map<String, Object> summary = new HashMap<>();
        summary.put("groupId", group.getId());
        summary.put("groupName", group.getName());
        summary.put("memberCount", group.getMembers().size());
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("balance", totalIncome - totalExpense);
        summary.put("isActive", group.getIsActive());
        summary.put("createdDate", group.getCreatedDate());

        return summary;
    }

    private String generateJoinCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Getter methods
    public List<Invitation> getPendingInvitations(String userEmail) {
        User user = findUserByEmail(userEmail);
        return invitationRepository.findByUserAndStatus(user, STATUS_PENDING);
    }

    public List<Group> getAvailableGroupsForUser(String userEmail) {
        User user = findUserByEmail(userEmail);
        return groupRepository.findAvailableGroupsForUser(user.getId());
    }

    public List<Group> getNewGroupsForUser(String userEmail) {
        User user = findUserByEmail(userEmail);
        return groupRepository.findNewGroupsByAdmin(user.getId());
    }

    public List<Group> getUserGroups(String userEmail) {
        User user = findUserByEmail(userEmail);
        return groupRepository.findGroupsByMemberId(user.getId());
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findById(Long id) {
        return groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
    }

    @Transactional
    public boolean deleteGroup(Long id) {
        try {
            Group group = findById(id);
            // Xóa các lời mời liên quan
            invitationRepository.deleteByGroupId(id);
            // Xóa nhóm
            groupRepository.delete(group);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Không thể xóa nhóm: " + e.getMessage());
        }
    }

    // Update and Delete operations
    @Transactional
    public Group updateGroup(Long id, Group newGroup) {
        return groupRepository.findById(id)
            .map(existing -> {
                existing.setName(newGroup.getName());
                existing.setDescription(newGroup.getDescription());
                existing.setIsActive(newGroup.getIsActive());
                existing.setMaxMembers(newGroup.getMaxMembers());
                existing.setGroupType(newGroup.getGroupType());
                return groupRepository.save(existing);
            })
            .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm"));
    }

}