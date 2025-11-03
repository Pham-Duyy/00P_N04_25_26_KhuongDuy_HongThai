package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.GroupInvite;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupInviteService;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test controller cho GroupUserController
 * Kiểm tra toàn bộ chức năng: xem lời mời, tham gia nhóm, rời nhóm, xem nhóm chi tiết...
 */
@WebMvcTest(GroupUserController.class)
class GroupUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupInviteService groupInviteService;

    private MockHttpSession session;
    private User testUser;
    private Group testGroup;
    private GroupInvite testInvite;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user1");
        testUser.setFullName("User Test");
        testUser.setRole(User.Role.USER);

        testGroup = new Group();
        testGroup.setId(100L);
        testGroup.setName("Nhóm Kiểm Thử");
        testGroup.setAdminId(2L);
        testGroup.setMembers(Collections.singletonList(testUser));

        testInvite = new GroupInvite();
        testInvite.setId(10L);
        testInvite.setGroup(testGroup);
        testInvite.setUser(testUser);

        session = new MockHttpSession();
        session.setAttribute("user", testUser);
    }

    // ===================== XEM LỜI MỜI =====================
    @Test
    void testViewInvites_ShouldReturnInviteListView() throws Exception {
        when(groupInviteService.findPendingInvitesByUser(1L)).thenReturn(Collections.singletonList(testInvite));

        mockMvc.perform(get("/user/groups/invites").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/invites"))
                .andExpect(model().attributeExists("invites"));
    }

    @Test
    void testViewInvites_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/groups/invites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== XEM NHÓM TỪ LỜI MỜI =====================
    @Test
    void testViewGroupFromInvite_ShouldReturnGroupDetailView() throws Exception {
        when(groupInviteService.findById(10L)).thenReturn(testInvite);

        mockMvc.perform(get("/user/groups/invites/10/group").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/group-detail"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void testViewGroupFromInvite_InvalidInvite_ShouldRedirect() throws Exception {
        when(groupInviteService.findById(10L)).thenReturn(null);

        mockMvc.perform(get("/user/groups/invites/10/group").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups/invites"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== DANH SÁCH NHÓM CỦA USER =====================
    @Test
    void testListMyGroups_ShouldReturnMyGroupsView() throws Exception {
        when(groupService.findGroupsByMember(1L)).thenReturn(Collections.singletonList(testGroup));

        mockMvc.perform(get("/user/groups").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/my-groups"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(model().attributeExists("adminCount"))
                .andExpect(model().attributeExists("memberCount"));
    }

    // ===================== XEM CHI TIẾT NHÓM =====================
    @Test
    void testViewJoinedGroup_ShouldReturnGroupDetail() throws Exception {
        when(groupService.findById(100L)).thenReturn(testGroup);

        mockMvc.perform(get("/user/groups/100").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/group-detail"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attributeExists("members"));
    }

    @Test
    void testViewJoinedGroup_WhenNotMember_ShouldRedirect() throws Exception {
        Group otherGroup = new Group();
        otherGroup.setId(200L);
        otherGroup.setMembers(Collections.emptyList());
        when(groupService.findById(200L)).thenReturn(otherGroup);

        mockMvc.perform(get("/user/groups/200").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== RỜI KHỎI NHÓM =====================
    @Test
    void testLeaveGroup_Success() throws Exception {
        when(groupService.findById(100L)).thenReturn(testGroup);

        mockMvc.perform(post("/user/groups/100/leave").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups"))
                .andExpect(flash().attributeExists("success"));

        verify(groupService, times(1)).removeMemberFromGroup(100L, 1L);
    }

    @Test
    void testLeaveGroup_WhenAdmin_ShouldBlockLeaving() throws Exception {
        testGroup.setAdminId(1L);
        when(groupService.findById(100L)).thenReturn(testGroup);

        mockMvc.perform(post("/user/groups/100/leave").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups/100"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== CHẤP NHẬN LỜI MỜI =====================
    @Test
    void testAcceptInvite_Success() throws Exception {
        when(groupInviteService.acceptInvite(10L, 1L)).thenReturn(true);

        mockMvc.perform(post("/user/groups/invites/10/accept").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups/invites"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    void testAcceptInvite_Failed() throws Exception {
        when(groupInviteService.acceptInvite(10L, 1L)).thenReturn(false);

        mockMvc.perform(post("/user/groups/invites/10/accept").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups/invites"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== TỪ CHỐI LỜI MỜI =====================
    @Test
    void testDeclineInvite_Success() throws Exception {
        doNothing().when(groupInviteService).declineInvite(10L, 1L);

        mockMvc.perform(post("/user/groups/invites/10/decline").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/groups/invites"))
                .andExpect(flash().attributeExists("success"));

        verify(groupInviteService, times(1)).declineInvite(10L, 1L);
    }
}
