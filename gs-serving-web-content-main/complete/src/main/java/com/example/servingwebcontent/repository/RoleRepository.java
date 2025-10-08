package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh tại đây nếu cần
}