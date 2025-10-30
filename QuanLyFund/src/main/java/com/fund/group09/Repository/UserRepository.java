package com.fund.group09.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fund.group09.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm user theo username
    Optional<User> findByUsername(String username);

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Kiểm tra tồn tại username
    boolean existsByUsername(String username);

    // Kiểm tra tồn tại email
    boolean existsByEmail(String email);

    // Lấy danh sách user theo vai trò
    List<User> findByRole(String role);

    // Tìm user theo email và role (dùng cho xác thực nâng cao)
    Optional<User> findByEmailAndRole(String email, String role);

}