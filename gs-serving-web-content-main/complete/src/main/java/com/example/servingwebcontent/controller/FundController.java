package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.Fund;
import com.example.servingwebcontent.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funds")
@CrossOrigin(origins = "*")
public class FundController {

    @Autowired
    private FundRepository fundRepository;

    @GetMapping
    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Fund> getFundById(@PathVariable Long id) {
        return fundRepository.findById(id);
    }

    @PostMapping
    public Fund createFund(@RequestBody Fund fund) {
        return fundRepository.save(fund);
    }

    @PutMapping("/{id}")
    public Fund updateFund(@PathVariable Long id, @RequestBody Fund fundDetails) {
        Fund fund = fundRepository.findById(id).orElseThrow();
        fund.setName(fundDetails.getName());
        fund.setTotalAmount(fundDetails.getTotalAmount());
        return fundRepository.save(fund);
    }

    @DeleteMapping("/{id}")
    public void deleteFund(@PathVariable Long id) {
        fundRepository.deleteById(id);
    }
}