package com.fund.group09.quanlyngansach.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.group09.quanlyngansach.Model.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
}