package com.fund.group09.quanlyngansach.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fund.group09.quanlyngansach.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
