package com.fund.group09.Controller;

import com.fund.group09.Model.Fund;
import com.fund.group09.Service.FundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funds")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API từ bất kỳ domain nào
public class FundController {

    private final FundService fundService;

    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    //  Lấy danh sách tất cả Fund
    @GetMapping
    public ResponseEntity<List<Fund>> getAllFunds() {
        List<Fund> funds = fundService.getAll();
        return ResponseEntity.ok(funds);
    }

    //  Lấy Fund theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable Long id) {
        Fund fund = fundService.getById(id);
        if (fund == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fund);
    }

    //  Thêm mới Fund
    @PostMapping
    public ResponseEntity<Fund> createFund(@RequestBody Fund fund) {
        if (fund.getBalance() == null || fund.getBalance() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Fund saved = fundService.save(fund);
        return ResponseEntity.status(201).body(saved);
    }

    //  Cập nhật Fund
    @PutMapping("/{id}")
    public ResponseEntity<Fund> updateFund(@PathVariable Long id, @RequestBody Fund newFund) {
        Fund updated = fundService.update(id, newFund);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    //  Xóa Fund
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        boolean deleted = fundService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
