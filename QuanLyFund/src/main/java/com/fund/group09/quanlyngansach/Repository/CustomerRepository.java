package com.fund.group09.quanlyngansach.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fund.group09.quanlyngansach.Model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Additional query methods can be defined here
}