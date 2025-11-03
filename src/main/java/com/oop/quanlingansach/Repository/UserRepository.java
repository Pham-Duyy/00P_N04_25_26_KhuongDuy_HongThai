package com.oop.quanlingansach.Repository;

import com.oop.quanlingansach.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Đếm số user theo vai trò
    long countByRole(User.Role role);
}