package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Transaction;
import com.oop.quanlingansach.Model.TransactionParticipant;
import com.oop.quanlingansach.Model.User;
import com.oop.quanlingansach.Repository.GroupRepository;
import com.oop.quanlingansach.Repository.TransactionParticipantRepository;
import com.oop.quanlingansach.Repository.TransactionRepository;
import com.oop.quanlingansach.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionParticipantRepository transactionParticipantRepository;

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public BigDecimal getTotalTransactionAmount() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAmountByType(String type) {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(t -> type.equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());
        return transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long countAllContributions() {
        return transactionParticipantRepository.count();
    }

    @Override
    public long countPaidContributions() {
        return transactionParticipantRepository.countByPaidTrue();
    }

    @Override
    public List<Map<String, Object>> getGroupStatistics() {
        var groups = groupRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (var group : groups) {
            Map<String, Object> map = new HashMap<>();
            map.put("group", group);
            map.put("totalMembers", group.getMembers() != null ? group.getMembers().size() : 0);
            map.put("totalTransactions", transactionRepository.findByGroupId(group.getId()).size());
            map.put("totalAmount", transactionRepository.findByGroupId(group.getId())
                    .stream()
                    .map(Transaction::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTransactionStatistics(String type, String status) {
        List<Transaction> transactions = transactionRepository.findAll();
        if (type != null && !type.isEmpty()) {
            transactions = transactions.stream()
                    .filter(t -> type.equalsIgnoreCase(t.getType()))
                    .collect(Collectors.toList());
        }
        // Nếu có status, bạn cần bổ sung trường status cho Transaction và lọc ở đây
        List<Map<String, Object>> result = new ArrayList<>();
        for (var t : transactions) {
            Map<String, Object> map = new HashMap<>();
            map.put("transaction", t);
            map.put("group", t.getGroup());
            map.put("amount", t.getAmount());
            map.put("participants", transactionParticipantRepository.findByTransactionId(t.getId()));
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getContributionStatistics(Long groupId) {
        List<TransactionParticipant> participants;
        if (groupId != null) {
            participants = transactionParticipantRepository.findByGroupId(groupId);
        } else {
            participants = transactionParticipantRepository.findAll();
        }
        Map<User, List<TransactionParticipant>> byUser = participants.stream()
                .collect(Collectors.groupingBy(TransactionParticipant::getUser));
        List<Map<String, Object>> result = new ArrayList<>();
        for (var entry : byUser.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", entry.getKey());
            map.put("totalContributions", entry.getValue().size());
            map.put("totalAmount", entry.getValue().stream()
                    .map(TransactionParticipant::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            map.put("paidCount", entry.getValue().stream().filter(TransactionParticipant::isPaid).count());
            map.put("unpaidCount", entry.getValue().stream().filter(tp -> !tp.isPaid()).count());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getUserActivityStatistics() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            map.put("groups", groupRepository.findByMembers_Id(user.getId()));
            map.put("contributions", transactionParticipantRepository.findByUserId(user.getId()));
            map.put("totalContributions", transactionParticipantRepository.findByUserId(user.getId()).size());
            map.put("totalAmount", transactionParticipantRepository.findByUserId(user.getId())
                    .stream()
                    .map(TransactionParticipant::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            result.add(map);
        }
        return result;
    }

    // Các phương thức trả về dữ liệu biểu đồ, bạn có thể tùy chỉnh theo nhu cầu
    @Override
    public Object getTransactionByMonthData() {
        // TODO: Triển khai logic thống kê giao dịch theo tháng
        return null;
    }

    @Override
    public Object getGroupActivityData() {
        // TODO: Triển khai logic thống kê hoạt động nhóm
        return null;
    }

    @Override
    public Object getPaymentStatusData() {
        // TODO: Triển khai logic thống kê trạng thái thanh toán
        return null;
    }
}