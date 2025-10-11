package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.model.Fund;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class FundRepository {
    private List<Fund> funds = new ArrayList<>();

    public List<Fund> findAll() {
        return funds;
    }

    public void save(Fund fund) {
        funds.add(fund);
    }

    public Fund findById(Long id) {
        for (Fund f : funds) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null;
    }

    public boolean update(Long id, Fund newFund) {
        Fund fund = findById(id);
        if (fund != null) {
            fund.setBalance(newFund.getBalance());
            fund.setGroup(newFund.getGroup());
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        Fund fund = findById(id);
        if (fund != null) {
            funds.remove(fund);
            return true;
        }
        return false;
    }
}
