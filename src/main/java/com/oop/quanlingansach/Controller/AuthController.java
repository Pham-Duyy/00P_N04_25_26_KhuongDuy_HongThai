package com.oop.quanlingansach.Controller;

import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // ==================== TRANG CHỦ ====================
    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        // Chuyển hướng theo vai trò
        if (user.getRole() == User.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    // ==================== ĐĂNG NHẬP ====================
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // Nếu đã đăng nhập, chuyển về trang chủ
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                             @RequestParam String password,
                             @RequestParam(required = false, defaultValue = "user") String userType,
                             HttpSession session, 
                             RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (username == null || username.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên đăng nhập!");
                return "redirect:/login";
            }
            
            if (password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập mật khẩu!");
                return "redirect:/login";
            }

            Optional<User> userOpt = userService.authenticate(username.trim(), password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // PHÂN QUYỀN: Kiểm tra userType có khớp với role không
                if ("admin".equals(userType) && user.getRole() != User.Role.ADMIN) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Tài khoản này không có quyền quản trị viên!");
                    return "redirect:/login";
                }
                if ("user".equals(userType) && user.getRole() != User.Role.USER) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Tài khoản này không phải là tài khoản người dùng thường!");
                    return "redirect:/login";
                }
                
                // Set session attributes
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userRole", user.getRole().name());
                session.setAttribute("userId", user.getId());
                session.setAttribute("fullName", user.getFullName());
                
                // Thông báo đăng nhập thành công
                String roleText = user.getRole() == User.Role.ADMIN ? "Quản trị viên" : "Người dùng";
                redirectAttributes.addFlashAttribute("success", 
                    "Chào mừng " + user.getFullName() + "! Đăng nhập thành công với quyền " + roleText + ".");
                
                // Redirect theo vai trò
                if (user.getRole() == User.Role.ADMIN) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/user/dashboard";
                }
            }
            
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra trong quá trình đăng nhập: " + e.getMessage());
            return "redirect:/login";
        }
    }

    // ==================== ĐĂNG KÝ ====================
    @GetMapping("/auth/register")
    public String showRegisterForm(Model model, HttpSession session) {
        // Nếu đã đăng nhập, chuyển về trang chủ
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String processRegister(@ModelAttribute("user") User user, BindingResult bindingResult,
                                   @RequestParam String confirmPassword,
                                   @RequestParam(required = false, defaultValue = "USER") String role,
                                   Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            // Validate input
            if (bindingResult.hasErrors()) {
                model.addAttribute("user", user);
                return "auth/register";
            }

            // Kiểm tra mật khẩu xác nhận
            if (!user.getPassword().equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
                return "redirect:/auth/register";
            }

            // Validate password strength
            if (user.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                return "redirect:/auth/register";
            }

            // Đặt role dựa vào lựa chọn của user
            if ("ADMIN".equals(role)) {
                user.setRole(User.Role.ADMIN);
            } else {
                user.setRole(User.Role.USER);
            }

            // Kiểm tra username đã tồn tại
            if (userService.existsByUsername(user.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "Tên đăng nhập đã tồn tại!");
                return "redirect:/auth/register";
            }

            // Kiểm tra email đã tồn tại
            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
                return "redirect:/auth/register";
            }

            userService.registerUser(user);
            
            String roleText = user.getRole() == User.Role.ADMIN ? "Quản trị viên" : "Người dùng";
            redirectAttributes.addFlashAttribute("success", 
                "Đăng ký thành công tài khoản " + roleText + "! Vui lòng đăng nhập để tiếp tục.");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    // ==================== ĐĂNG XUẤT ====================
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        String fullName = "";
        User user = (User) session.getAttribute("user");
        if (user != null) {
            fullName = user.getFullName();
        }
        
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", 
            "Tạm biệt " + fullName + "! Đăng xuất thành công.");
        return "redirect:/login";
    }

    @GetMapping("/auth/logout")
    public String authLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        return logout(session, redirectAttributes);
    }

    // ==================== PROFILE ====================
    @GetMapping("/auth/profile")
    public String showProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        // Lấy thông tin user mới nhất từ DB
        Optional<User> userOpt = userService.getUserById(user.getId());
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
        } else {
            model.addAttribute("user", user);
        }
        
        return "auth/profile";
    }

    @PostMapping("/auth/profile")
    public String updateProfile(@ModelAttribute("user") User userDetails,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDetails);
            return "auth/profile";
        }

        try {
            // Kiểm tra email đã tồn tại (trừ email hiện tại)
            if (!userDetails.getEmail().equals(sessionUser.getEmail()) && 
                userService.existsByEmail(userDetails.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng!");
                return "redirect:/auth/profile";
            }

            Optional<User> updatedUser = userService.updateUser(sessionUser.getId(), userDetails);
            if (updatedUser.isPresent()) {
                // Cập nhật session
                session.setAttribute("user", updatedUser.get());
                session.setAttribute("fullName", updatedUser.get().getFullName());
                redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không thể cập nhật thông tin!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/auth/profile";
    }

    // ==================== ĐỔI MẬT KHẨU ====================
    @PostMapping("/auth/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            // Validate input
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập mật khẩu cũ!");
                return "redirect:/auth/profile";
            }

            if (newPassword == null || newPassword.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập mật khẩu mới!");
                return "redirect:/auth/profile";
            }

            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                return "redirect:/auth/profile";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận không khớp!");
                return "redirect:/auth/profile";
            }

            if (oldPassword.equals(newPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải khác mật khẩu cũ!");
                return "redirect:/auth/profile";
            }

            boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
                // Đăng xuất để buộc đăng nhập lại với mật khẩu mới
                session.invalidate();
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/auth/profile";
    }

    // ==================== API ENDPOINTS ====================
    @GetMapping("/auth/check-username")
    @ResponseBody
    public boolean checkUsernameExists(@RequestParam String username) {
        return userService.existsByUsername(username);
    }

    @GetMapping("/auth/check-email")
    @ResponseBody
    public boolean checkEmailExists(@RequestParam String email) {
        return userService.existsByEmail(email);
    }
}