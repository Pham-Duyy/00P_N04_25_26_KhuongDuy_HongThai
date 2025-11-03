package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test controller cho AuthController
 * Kiểm tra các luồng đăng nhập, đăng ký, đăng xuất, và phân quyền người dùng.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private MockHttpSession session;
    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setup() {
        session = new MockHttpSession();

        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setFullName("Admin User");
        adminUser.setRole(User.Role.ADMIN);

        normalUser = new User();
        normalUser.setId(2L);
        normalUser.setUsername("user");
        normalUser.setFullName("Normal User");
        normalUser.setRole(User.Role.USER);
    }

    // ===================== HOME =====================
    @Test
    void testHomeRedirect_WhenUserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testHomeRedirect_WhenAdminLoggedIn() throws Exception {
        session.setAttribute("user", adminUser);
        mockMvc.perform(get("/").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void testHomeRedirect_WhenUserLoggedIn() throws Exception {
        session.setAttribute("user", normalUser);
        mockMvc.perform(get("/").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));
    }

    // ===================== LOGIN =====================
    @Test
    void testShowLoginForm_WhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void testShowLoginForm_WhenAlreadyLoggedIn() throws Exception {
        session.setAttribute("user", normalUser);
        mockMvc.perform(get("/login").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testProcessLogin_SuccessForUser() throws Exception {
        when(userService.authenticate(eq("user"), eq("123456")))
                .thenReturn(Optional.of(normalUser));

        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "123456")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/dashboard"));
    }

    @Test
    void testProcessLogin_SuccessForAdmin() throws Exception {
        when(userService.authenticate(eq("admin"), eq("123456")))
                .thenReturn(Optional.of(adminUser));

        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "123456")
                        .param("userType", "admin")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void testProcessLogin_FailedWrongPassword() throws Exception {
        when(userService.authenticate(eq("user"), eq("wrong")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ===================== REGISTER =====================
    @Test
    void testShowRegisterForm_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    void testProcessRegister_Success() throws Exception {
        when(userService.existsByUsername("newuser")).thenReturn(false);
        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        Mockito.doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/auth/register")
                        .param("username", "newuser")
                        .param("email", "test@example.com")
                        .param("password", "123456")
                        .param("confirmPassword", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testProcessRegister_PasswordMismatch() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .param("username", "newuser")
                        .param("password", "123456")
                        .param("confirmPassword", "654321"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/register"));
    }

    // ===================== LOGOUT =====================
    @Test
    void testLogout_ShouldInvalidateSessionAndRedirect() throws Exception {
        session.setAttribute("user", normalUser);

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ===================== PROFILE =====================
    @Test
    void testShowProfile_WhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/auth/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}