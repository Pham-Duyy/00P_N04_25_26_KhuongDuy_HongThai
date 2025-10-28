package com.fund.group09.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Thêm import này
import org.springframework.security.crypto.password.PasswordEncoder;   // Thêm import này
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu dùng API
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Cho phép tất cả request
            )
            .formLogin(form -> form.disable()); // Tắt form login mặc định

        return http.build();
    }

    // Bean PasswordEncoder để dùng cho mã hóa và kiểm tra mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}