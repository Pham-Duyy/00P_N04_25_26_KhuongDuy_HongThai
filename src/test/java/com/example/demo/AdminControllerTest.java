package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test controller cho lớp AdminController
 * Kiểm tra quyền truy cập và view trả về đúng.
 */
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession adminSession;
    private MockHttpSession userSession;

    @BeforeEach
    void setUp() {
        // Tạo session cho admin
        User admin = new User();
        admin.setName("Admin User");
        admin.setRole(User.Role.ADMIN);
        adminSession = new MockHttpSession();
        adminSession.setAttribute("user", admin);

        // Tạo session cho user thường
        User user = new User();
        user.setName("Normal User");
        user.setRole(User.Role.USER);
        userSession = new MockHttpSession();
        userSession.setAttribute("user", user);
    }

    //  Truy cập dashboard với quyền admin -> cho phép
    @Test
    void testAdminDashboard_WithAdminRole_ShouldReturnDashboardView() throws Exception {
        mockMvc.perform(get("/admin/dashboard").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("user"));
    }

    //  Truy cập dashboard không đăng nhập -> bị redirect
    @Test
    void testAdminDashboard_WithoutLogin_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    //  Truy cập dashboard bằng user thường -> bị redirect
    @Test
    void testAdminDashboard_WithUserRole_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/admin/dashboard").session(userSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    //  Quản lý thu chi - đúng quyền
    @Test
    void testManageTransactions_WithAdminRole_ShouldReturnTransactionsView() throws Exception {
        mockMvc.perform(get("/admin/transactions").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/transactions"))
                .andExpect(model().attributeExists("user"));
    }

    // Báo cáo thống kê
    @Test
    void testViewReports_WithAdminRole_ShouldReturnReportsView() throws Exception {
        mockMvc.perform(get("/admin/reports").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reports"))
                .andExpect(model().attributeExists("user"));
    }

    //  Thông báo
    @Test
    void testViewNotifications_WithAdminRole_ShouldReturnNotificationsView() throws Exception {
        mockMvc.perform(get("/admin/notifications").session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/notifications"))
                .andExpect(model().attributeExists("user"));
    }

    // Truy cập /admin gốc -> redirect về /admin/dashboard
    @Test
    void testAdminHome_ShouldRedirectToDashboard() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }
}
