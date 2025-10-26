package com.fund.group09.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "users/login"; // Thêm users/ vào trước
    }

    @GetMapping("/register")
    public String registerPage() {
        return "users/register"; // Thêm users/ vào trước
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}