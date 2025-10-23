package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.Fund;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class FundService {
    private List<Fund> funds = new ArrayList<>();

    public List<Fund> getAllFunds() {
        return funds;
    }

    public Fund addFund(Fund fund) {
        funds.add(fund);
        return fund;
    }

    public Fund updateFund(Long id, Fund newFund) {
        for (Fund fund : funds) {
            if (fund.getId().equals(id)) {
                fund.setBalance(newFund.getBalance());
                fund.setGroup(newFund.getGroup());
                return fund;
            }
        }
        return null;
    }

    public boolean deleteFund(Long id) {
        return funds.removeIf(f -> f.getId().equals(id));
    }

    public Fund findFundById(Long id) {
        for (Fund f : funds) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null;
    }
}
