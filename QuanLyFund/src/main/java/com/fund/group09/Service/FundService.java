package com.fund.group09.Service;

import com.fund.group09.Model.Fund;
import com.fund.group09.Repository.FundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FundService {

    private final FundRepository fundRepository;

    public FundService(FundRepository fundRepository) {
        this.fundRepository = fundRepository;
    }

    @Transactional(readOnly = true)
    public List<Fund> getAll() {
        return fundRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Fund> getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống");
        }
        return fundRepository.findById(id);
    }

    public Fund save(Fund fund) {
        if (fund == null) {
            throw new IllegalArgumentException("Fund không được để trống");
        }
        return fundRepository.save(fund);
    }

    public Optional<Fund> update(Long id, Fund newFund) {
        if (id == null || newFund == null) {
            throw new IllegalArgumentException("ID và Fund không được để trống");
        }

        return fundRepository.findById(id)
            .map(existingFund -> {
                existingFund.setBalance(newFund.getBalance());
                existingFund.setDescription(newFund.getDescription());
                existingFund.setGroup(newFund.getGroup());
                return fundRepository.save(existingFund);
            });
    }

    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID không được để trống");
        }

        if (!fundRepository.existsById(id)) {
            return false;
        }
        
        try {
            fundRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Tiện ích kiểm tra tồn tại
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }
        return fundRepository.existsById(id);
    }
}