package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.UserService;
import com.oop.quanlingansach.Service.GroupService;
import com.oop.quanlingansach.Service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private TransactionService transactionService;

    private MockHttpSession adminSession;
    private MockHttpSession userSession;

    @BeforeEach
    void setUp() {
        // Tạo session cho admin
        User admin = new User();
        admin.setFullName("Admin User");
        admin.setRole(User.Role.ADMIN);
        adminSession = new MockHttpSession();
        adminSession.setAttribute("user", admin);

        // Tạo session cho user thường
        User user = new User();
        user.setFullName("Normal User");
        user.setRole(User.Role.USER);
        userSession = new MockHttpSession();
        userSession.setAttribute("user", user);
    }

    @Test
    void testAdminDashboard_WithAdminRole_ShouldReturnDashboardView() throws Exception {
        mockMvc.perform(get("/admin/dashboard").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testAdminDashboard_WithoutLogin_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testAdminDashboard_WithUserRole_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard").session(userSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }


    @Test
    void testAdminHome_ShouldRedirectToDashboard() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }
}