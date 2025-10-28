package com.fund.group09.Service;

import java.util.List;
import com.fund.group09.Model.Invitation;
import com.fund.group09.Model.User;
import com.fund.group09.Model.Group;

public interface InvitationService {
    // Lấy lời mời chờ xác nhận theo email
    List<Invitation> getPendingInvitationsForUser(String userEmail);

    // Lấy lời mời chờ xác nhận theo user entity
    List<Invitation> getPendingInvitationsForUser(User user);

    // Lấy lời mời chờ xác nhận theo userId
    List<Invitation> getPendingInvitationsForUserId(Long userId);

    // Lấy lời mời chờ xác nhận theo group
    List<Invitation> getPendingInvitationsForGroup(Group group);

    // Lấy lời mời chờ xác nhận theo groupId
    List<Invitation> getPendingInvitationsForGroupId(Long groupId);

    // Chấp nhận lời mời (có thể truyền thêm userEmail để xác thực)
    boolean acceptInvitation(Long invitationId, String userEmail);

    // Từ chối lời mời (có thể truyền thêm userEmail để xác thực)
    boolean rejectInvitation(Long invitationId, String userEmail);
}