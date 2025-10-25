package com.fund.group09.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // trả về login.html trong templates
    }

    @GetMapping({"/", "/index"})
    public String indexPage() {
        return "index"; // trả về index.html trong templates
    }
}