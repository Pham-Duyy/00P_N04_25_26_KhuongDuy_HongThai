package com.fund.group09.Controller;

import com.fund.group09.Model.Fund;
import com.fund.group09.Service.FundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/funds")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class FundController {

    private final FundService fundService;

    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    // Lấy danh sách tất cả các quỹ
    @GetMapping
    public ResponseEntity<List<Fund>> getAllFunds() {
        List<Fund> danhSachQuy = fundService.getAll();
        return ResponseEntity.ok(danhSachQuy);
    }

    // Lấy thông tin quỹ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable Long id) {
        Optional<Fund> quy = fundService.getById(id);
        if (quy.isPresent()) {
            return ResponseEntity.ok(quy.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Tạo quỹ mới
    @PostMapping
    public ResponseEntity<Fund> createFund(@Valid @RequestBody Fund fund) {
        // Kiểm tra số dư không âm
        if (fund.getBalance() == null || 
            fund.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().build();
        }
        
        Fund quyMoi = fundService.save(fund);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(quyMoi);
    }

    // Cập nhật thông tin quỹ
    @PutMapping("/{id}")
    public ResponseEntity<Fund> updateFund(
            @PathVariable Long id,
            @Valid @RequestBody Fund quyCapNhat) {
        Optional<Fund> quyDaCapNhat = fundService.update(id, quyCapNhat);
        if (quyDaCapNhat.isPresent()) {
            return ResponseEntity.ok(quyDaCapNhat.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa quỹ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        boolean daXoa = fundService.delete(id);
        if (daXoa) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Xử lý lỗi validation
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(
            jakarta.validation.ConstraintViolationException e) {
        return ResponseEntity
                .badRequest()
                .body("Lỗi validation: " + e.getMessage());
    }
}