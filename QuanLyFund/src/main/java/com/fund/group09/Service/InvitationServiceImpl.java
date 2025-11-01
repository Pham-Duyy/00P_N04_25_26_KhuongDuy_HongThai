package com.fund.group09.Service;

import com.fund.group09.Model.Invitation;
import com.fund.group09.Model.User;
import com.fund.group09.Model.Group;
import com.fund.group09.Repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Override
    public List<Invitation> getPendingInvitationsForUser(String userEmail) {
        return invitationRepository.findByUser_EmailAndStatus(userEmail, "PENDING");
    }

    @Override
    public List<Invitation> getPendingInvitationsForUser(User user) {
        return invitationRepository.findByUserAndStatus(user, "PENDING");
    }

    @Override
    public List<Invitation> getPendingInvitationsForUserId(Long userId) {
        // Nếu bạn có hàm findByUserIdAndStatus trong repository thì dùng, nếu không thì viết thêm
        // return invitationRepository.findByUserIdAndStatus(userId, "PENDING");
        return List.of(); // Bổ sung nếu cần
    }

    @Override
    public List<Invitation> getPendingInvitationsForGroup(Group group) {
        return invitationRepository.findByGroupAndStatus(group, "PENDING");
    }

    @Override
    public List<Invitation> getPendingInvitationsForGroupId(Long groupId) {
        // Nếu bạn có hàm findByGroupIdAndStatus trong repository thì dùng, nếu không thì viết thêm
        // return invitationRepository.findByGroupIdAndStatus(groupId, "PENDING");
        return List.of(); // Bổ sung nếu cần
    }

    @Override
    public boolean acceptInvitation(Long invitationId, String userEmail) {
        // Cài đặt logic accept invitation ở đây
        return true;
    }

    @Override
    public int countPendingInvitationsForUser(String userEmail) {
        return getPendingInvitationsForUser(userEmail).size();
    }

    @Override
    public boolean rejectInvitation(Long invitationId, String userEmail) {
        // Cài đặt logic reject invitation ở đây
        return true;
    }
}