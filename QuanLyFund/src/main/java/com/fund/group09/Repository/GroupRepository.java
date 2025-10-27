package com.fund.group09.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fund.group09.Model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    // Tìm kiếm theo tên hoặc mô tả (không phân biệt hoa thường)
    List<Group> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Page<Group> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc, Pageable pageable);
    Page<Group> findAll(Pageable pageable);
}