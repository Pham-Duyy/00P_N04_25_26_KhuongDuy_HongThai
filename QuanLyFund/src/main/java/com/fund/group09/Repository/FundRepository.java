package com.fund.group09.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.group09.Model.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
}