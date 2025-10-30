package com.fund.group09.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  Số tiền thu
    private Double amount;

    // Ngày thu
    private LocalDate date;

    //  Mô tả khoản thu
    private String description;

    //  Thành viên nào thu
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //  Mỗi khoản thu có thể ảnh hưởng đến quỹ (nếu có)
    @ManyToOne
    @JoinColumn(name = "fund_id")
    private Fund fund;

    //  Constructor
    public Income() {}

    public Income(Double amount, LocalDate date, String description, Member member, Fund fund) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.member = member;
        this.fund = fund;
    }

    //  Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }
}
