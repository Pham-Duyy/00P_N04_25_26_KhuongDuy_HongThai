package com.fund.group09.Repository;

import com.fund.group09.Model.Invitation;
import com.fund.group09.Model.User;
import com.fund.group09.Model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByUserAndStatus(User user, String status);
    List<Invitation> findByGroupAndStatus(Group group, String status);
    void deleteByGroupId(Long groupId);
    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
    List<Invitation> findByUser_EmailAndStatus(String email, String status);
    
}