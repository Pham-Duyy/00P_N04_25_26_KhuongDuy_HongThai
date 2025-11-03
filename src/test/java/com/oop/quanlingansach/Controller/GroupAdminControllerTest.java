package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.Group;
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
 * Test controller cho GroupAdminController
 * Kiểm tra các chức năng: danh sách nhóm, tạo nhóm, cập nhật, xem chi tiết, xóa nhóm.
 */
@WebMvcTest(GroupAdminController.class)
class GroupAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupInviteService groupInviteService;

    private MockHttpSession adminSession;
    private User adminUser;
    private Group sampleGroup;

    @BeforeEach
    void setUp() {
        // Tạo admin session
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setFullName("Admin User");
        adminUser.setRole(User.Role.ADMIN);

        adminSession = new MockHttpSession();
        adminSession.setAttribute("user", adminUser);

        // Tạo group mẫu
        sampleGroup = new Group();
        sampleGroup.setId(10L);
        sampleGroup.setName("Nhóm Kiểm Thử");
        sampleGroup.setDescription("Nhóm thử nghiệm JUnit");
    }

    // ===================== DANH SÁCH NHÓM =====================
    @Test
    void testListGroups_ShouldReturnGroupListView() throws Exception {
        List<Group> groups = Collections.singletonList(sampleGroup);
        List<User> users = Collections.singletonList(adminUser);

        when(groupService.findAll()).thenReturn(groups);
        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/groups/group-create"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("group"));
    }

    @Test
    void testListGroups_WithKeyword_ShouldSearchGroups() throws Exception {
        when(groupService.searchByName("Java")).thenReturn(Arrays.asList(sampleGroup));
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(adminUser));

        mockMvc.perform(get("/admin/groups").param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/groups/group-create"))
                .andExpect(model().attributeExists("groups"));
    }

    // ===================== TẠO NHÓM =====================
    

    @Test
    void testCreateGroup_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/admin/groups/create")
                        .param("name", "Nhóm Không Hợp Lệ"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== CẬP NHẬT NHÓM =====================
    @Test
    void testUpdateGroup_ShouldUpdateAndRedirect() throws Exception {
        doNothing().when(groupService).updateGroup(eq(10L), any(Group.class));

        mockMvc.perform(post("/admin/groups/10/edit")
                        .param("name", "Tên Mới")
                        .param("description", "Mô tả mới"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/groups"))
                .andExpect(flash().attributeExists("success"));
    }

    // ===================== XEM CHI TIẾT NHÓM =====================
    @Test
    void testViewGroup_ShouldReturnDetailView() throws Exception {
        when(groupService.findById(10L)).thenReturn(sampleGroup);

        mockMvc.perform(get("/admin/groups/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/groups/group-detail"))
                .andExpect(model().attributeExists("group"));
    }

    // ===================== API JSON =====================
    @Test
    void testGetGroupJson_ShouldReturnGroupJson() throws Exception {
        when(groupService.findById(10L)).thenReturn(sampleGroup);

        mockMvc.perform(get("/admin/groups/10/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Nhóm Kiểm Thử"));
    }

    // ===================== XÓA NHÓM =====================
    @Test
    void testDeleteGroup_ShouldDeleteAndRedirect() throws Exception {
        doNothing().when(groupService).deleteGroup(10L);

        mockMvc.perform(post("/admin/groups/10/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/groups"))
                .andExpect(flash().attributeExists("success"));

        verify(groupService, times(1)).deleteGroup(10L);
    }

    // ===================== HÀM PHỤ =====================
    private User createUser(Long id, String username, User.Role role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFullName(username);
        user.setRole(role);
        return user;
    }
}