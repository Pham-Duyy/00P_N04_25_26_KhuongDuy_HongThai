package com.fund.group09.quanlyngansach.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.group09.quanlyngansach.Model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional query methods can be defined here if needed
}