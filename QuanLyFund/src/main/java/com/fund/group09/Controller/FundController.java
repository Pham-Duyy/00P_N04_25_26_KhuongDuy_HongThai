package com.fund.group09.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fund.group09.Model.Fund;
import com.fund.group09.Repository.FundRepository;

@RestController
@RequestMapping("/funds")
@CrossOrigin(origins = "*")
public class FundController {

    @Autowired
    private FundRepository fundRepository;

    // ✅ Lấy tất cả fund
    @GetMapping
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    // ✅ Lấy fund theo id
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable Long id) {
        return fundRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Thêm fund mới
    @PostMapping
    public ResponseEntity<Fund> addFund(@RequestBody Fund fund) {
        Fund saved = fundRepository.save(fund);
        return ResponseEntity.status(201).body(saved);
    }

    // ✅ Cập nhật fund
    @PutMapping("/{id}")
    public ResponseEntity<Fund> updateFund(@PathVariable Long id, @RequestBody Fund newFund) {
        return fundRepository.findById(id)
                .map(fund -> {
                    fund.setBalance(newFund.getBalance());
                    fund.setGroup(newFund.getGroup());
                    Fund updated = fundRepository.save(fund);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Xóa fund
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        if (!fundRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fundRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
