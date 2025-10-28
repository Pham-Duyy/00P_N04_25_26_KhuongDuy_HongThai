package com.fund.group09.Service;

import com.fund.group09.Model.*;
import com.fund.group09.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final InvitationRepository invitationRepository;

    public MemberService(MemberRepository memberRepository,
                         UserRepository userRepository,
                         GroupRepository groupRepository,
                         ExpenseRepository expenseRepository,
                         IncomeRepository incomeRepository,
                         InvitationRepository invitationRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
        this.invitationRepository = invitationRepository;
    }

    // Lấy tất cả thành viên
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Lấy thành viên theo ID
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên"));
    }

    // Xử lý chấp nhận lời mời tham gia nhóm
    @Transactional
    public Member acceptInvitation(Long invitationId, String userEmail) {
        Invitation invitation = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời"));

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!invitation.getUser().equals(user)) {
            throw new RuntimeException("Không có quyền xử lý lời mời này");
        }

        if (!"PENDING".equals(invitation.getStatus())) {
            throw new RuntimeException("Lời mời không còn hiệu lực");
        }

        Group group = invitation.getGroup();

        // Kiểm tra số lượng thành viên
        if (memberRepository.countByGroupId(group.getId()) >= group.getMaxMembers()) {
            throw new RuntimeException("Nhóm đã đầy");
        }

        // Tạo thành viên mới
        Member newMember = new Member();
        newMember.setUser(user);
        newMember.setGroup(group);
        newMember.setName(user.getName());
        newMember.setEmail(user.getEmail());
        newMember.setRole("MEMBER");
        LocalDateTime now = LocalDateTime.now();
        newMember.setJoinedAt(now); // Gán ngày xác nhận tham gia
        System.out.println("DEBUG: Setting joinedAt to " + now); // Log debug

        // Cập nhật trạng thái lời mời
        invitation.setStatus("ACCEPTED");
        invitation.setResponseDate(now);
        invitationRepository.save(invitation);

        Member savedMember = memberRepository.save(newMember);
        System.out.println("DEBUG: Saved member with joinedAt: " + savedMember.getJoinedAt()); // Log debug
        return savedMember;
    }

    // Từ chối lời mời
    @Transactional
    public void rejectInvitation(Long invitationId, String userEmail) {
        Invitation invitation = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời"));

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!invitation.getUser().equals(user)) {
            throw new RuntimeException("Không có quyền xử lý lời mời này");
        }

        invitation.setStatus("REJECTED");
        invitation.setResponseDate(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    // Lấy danh sách lời mời đang chờ của user
    public List<Invitation> getPendingInvitations(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return invitationRepository.findByUserAndStatus(user, "PENDING");
    }

    // Lấy danh sách thành viên theo nhóm
    public List<Member> getMembersByGroup(Long groupId) {
        List<Member> members = memberRepository.findByGroupId(groupId);
        for (Member member : members) {
            if (member.getJoinedAt() == null) {
                // Tự động set joinedAt nếu null (cho member cũ)
                member.setJoinedAt(member.getGroup().getCreatedDate());
                memberRepository.save(member);
                System.out.println("DEBUG: Updated joinedAt for member " + member.getId());
            }
        }
        return members;
    }

    // Rời khỏi nhóm
    @Transactional
    public void leaveGroup(Long groupId, String userEmail) {
        Member member = memberRepository.findByGroupIdAndUserEmail(groupId, userEmail)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy thành viên trong nhóm"));

        if ("ADMIN".equals(member.getRole())) {
            throw new RuntimeException("Admin không thể rời nhóm");
        }

        memberRepository.delete(member);
    }

    public boolean isMemberOfGroup(Long groupId, String userEmail) {
        if (userEmail == null) return false;
        return memberRepository.existsByGroupIdAndEmail(groupId, userEmail);
    }

    // Tính toán tổng thu, chi và số dư của thành viên
    public Map<String, Object> getMemberSummary(Long memberId) {
        Member member = getMemberById(memberId);

        Double totalExpense = expenseRepository.sumAmountByMemberId(memberId);
        Double totalIncome = incomeRepository.sumAmountByMemberId(memberId);

        totalExpense = totalExpense != null ? totalExpense : 0.0;
        totalIncome = totalIncome != null ? totalIncome : 0.0;

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("memberId", member.getId());
        summary.put("memberName", member.getName());
        summary.put("email", member.getEmail());
        summary.put("groupName", member.getGroup().getName());
        summary.put("role", member.getRole());
        summary.put("totalIncome", BigDecimal.valueOf(totalIncome));
        summary.put("totalExpense", BigDecimal.valueOf(totalExpense));
        summary.put("balance", BigDecimal.valueOf(totalIncome - totalExpense));

        return summary;
    }
}