package com.example.servingwebcontent.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.example.servingwebcontent.model.Fund;

@RestController
@RequestMapping("/funds")
public class FundController {
    // Danh sách lưu trữ các fund (giả lập database)
    private List<Fund> funds = new ArrayList<>();

    // Lấy tất cả fund
    @GetMapping
    public List<Fund> getAllFunds() {
        return funds;
    }

    // Thêm mới fund
    @PostMapping
    public Fund addFund(@RequestBody Fund fund) {
        funds.add(fund);
        return fund;
    }

    // Sửa fund theo id
    @PutMapping("/{id}")
    public Fund updateFund(@PathVariable Long id, @RequestBody Fund newFund) {
        Fund fund = findFundById(id);
        if (fund != null) {
            fund.setBalance(newFund.getBalance());
            fund.setGroup(newFund.getGroup());
            return fund;
        }
        // Không tìm thấy fund
        return null;
    }

    // Xóa fund theo id
    @DeleteMapping("/{id}")
    public String deleteFund(@PathVariable Long id) {
        Fund fund = findFundById(id);
        if (fund != null) {
            funds.remove(fund);
            return "Xóa thành công";
        }
        return "Không tìm thấy fund";
    }

    // Hàm tiện ích tìm fund theo id
    private Fund findFundById(Long id) {
        for (Fund f : funds) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null;
    }
}
