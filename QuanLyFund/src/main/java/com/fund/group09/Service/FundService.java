package com.fund.group09.Service;

import com.fund.group09.Model.Fund;
import com.fund.group09.Repository.FundRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundService {

    private final FundRepository repo;

    public FundService(FundRepository repo) {
        this.repo = repo;
    }

    public List<Fund> getAll() {
        return repo.findAll();
    }

    public Fund getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Fund save(Fund fund) {
        return repo.save(fund);
    }

    public Fund update(Long id, Fund newFund) {
        return repo.findById(id).map(fund -> {
            fund.setBalance(newFund.getBalance());
            fund.setDescription(newFund.getDescription());
            fund.setGroup(newFund.getGroup());
            return repo.save(fund);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
