package com.example.servingwebcontent.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    private Long id;
    private String description;
    private Double amount;
}
