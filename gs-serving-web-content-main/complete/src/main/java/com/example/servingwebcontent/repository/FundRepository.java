package com.example.servingwebcontent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.servingwebcontent.model.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
    // Có thể thêm các phương thức truy vấn tùy chỉnh tại đây
}