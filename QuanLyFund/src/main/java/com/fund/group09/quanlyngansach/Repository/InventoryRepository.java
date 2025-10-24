package com.fund.group09.quanlyngansach.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.group09.quanlyngansach.Model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // Additional query methods can be defined here
}