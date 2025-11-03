package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
import com.oop.quanlingansach.Model.GroupInvite;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.GroupInviteService;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        ReflectionTestUtils.setField(testInvite, "id", 10L);
        testInvite.setGroup(testGroup);
        testInvite.setUser(testUser);

        session = new MockHttpSession();
        session.setAttribute("user", testUser);
    }

    @Test
    void testViewInvites_ShouldReturnInviteListView() throws Exception {
        when(groupInviteService.findPendingInvitesByUser(1L)).thenReturn(Collections.singletonList(testInvite));

        mockMvc.perform(get("/user/groups/invites").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/invites"))
                .andExpect(model().attributeExists("invites"));
    }

    @Test
    void testListMyGroups_ShouldReturnMyGroupsView() throws Exception {
        when(groupService.findGroupsByMember(1L)).thenReturn(Collections.singletonList(testGroup));

        mockMvc.perform(get("/user/groups").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/my-groups"))
                .andExpect(model().attributeExists("groups"));
    }

    @Test
    void testViewJoinedGroup_ShouldReturnGroupDetail() throws Exception {
        when(groupService.findById(100L)).thenReturn(testGroup);

        mockMvc.perform(get("/user/groups/100").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/groups/group-detail"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void testViewInvites_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/groups/invites"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}