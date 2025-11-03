package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test controller cho UserController
 * Kiểm tra các luồng đăng nhập, dashboard, profile, settings và API trạng thái.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user1");
        testUser.setFullName("User Kiểm Thử");
        testUser.setRole(User.Role.USER);

        session = new MockHttpSession();
        session.setAttribute("user", testUser);
    }

    // ===================== DASHBOARD =====================
    @Test
    void testUserDashboard_WhenLoggedIn_ShouldReturnDashboardView() throws Exception {
        mockMvc.perform(get("/user/dashboard").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/dashboard"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("notifications"));
    }

    @Test
    void testUserDashboard_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== PROFILE =====================
    @Test
    void testProfile_WhenLoggedIn_ShouldReturnProfileView() throws Exception {
        mockMvc.perform(get("/user/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testProfile_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== SETTINGS =====================
    @Test
    void testSettings_WhenLoggedIn_ShouldReturnSettingsView() throws Exception {
        mockMvc.perform(get("/user/settings").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("user/settings"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testSettings_WhenNotLoggedIn_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/settings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("error"));
    }

    // ===================== API STATUS =====================
    @Test
    void testApiStatus_WhenLoggedIn_ShouldReturnStatusMessage() throws Exception {
        mockMvc.perform(get("/user/api/status").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Đã đăng nhập với tài khoản: user1"));
    }

    @Test
    void testApiStatus_WhenNotLoggedIn_ShouldReturnNotLoggedInMessage() throws Exception {
        mockMvc.perform(get("/user/api/status"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chưa đăng nhập"));
    }
}