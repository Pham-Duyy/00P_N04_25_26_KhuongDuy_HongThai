package com.oop.quanlingansach.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportService {
    long countAllUsers();
    BigDecimal getTotalTransactionAmount();
    BigDecimal getTotalAmountByType(String type);
    long countAllContributions();
    long countPaidContributions();
    List<Map<String, Object>> getGroupStatistics();
    List<Map<String, Object>> getTransactionStatistics(String type, String status);
    List<Map<String, Object>> getContributionStatistics(Long groupId);
    List<Map<String, Object>> getUserActivityStatistics();
    Object getTransactionByMonthData();
    Object getGroupActivityData();
    Object getPaymentStatusData();
}