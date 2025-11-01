package com.fund.group09.Service;

import com.fund.group09.Model.*;
import com.fund.group09.Repository.TransactionRepository;
import com.fund.group09.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private NotificationService notificationService;

    // ✅ Lấy tất cả giao dịch với JOIN FETCH để tránh N+1 problem
    public List<Transaction> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionRepository.findAllWithDetails();
            System.out.println("✅ Loaded " + transactions.size() + " transactions from database with JOIN FETCH");
            
            for (Transaction tx : transactions) {
                System.out.println("   - Transaction ID: " + tx.getId() + 
                                 ", Type: " + tx.getType() + 
                                 ", Amount: " + tx.getAmount() + 
                                 ", Group: " + (tx.getGroup() != null ? tx.getGroup().getName() : "N/A") +
                                 ", Status: " + tx.getStatus());
            }
            
            return transactions;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy tất cả giao dịch: " + e.getMessage());
            e.printStackTrace();
            try {
                return transactionRepository.findAll();
            } catch (Exception fallbackEx) {
                System.err.println("❌ Fallback cũng lỗi: " + fallbackEx.getMessage());
                return new ArrayList<>();
            }
        }
    }

    // ✅ Lấy giao dịch theo nhóm - SỬ DỤNG JOIN FETCH
    public List<Transaction> getTransactionsByGroup(Long groupId) {
        try {
            if (groupId == null) {
                return getAllTransactions();
            }
            
            List<Transaction> transactions = transactionRepository.findByGroupIdWithDetails(groupId);
            System.out.println("✅ Loaded " + transactions.size() + " transactions for group " + groupId + " with JOIN FETCH");
            
            for (Transaction tx : transactions) {
                System.out.println("   - Transaction ID: " + tx.getId() + 
                                 ", Type: " + tx.getType() + 
                                 ", Amount: " + tx.getAmount() + 
                                 ", Description: " + tx.getDescription());
            }
            
            return transactions;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy giao dịch theo nhóm " + groupId + ": " + e.getMessage());
            e.printStackTrace();
            try {
                return transactionRepository.findByGroupId(groupId);
            } catch (Exception fallbackEx) {
                System.err.println("❌ Fallback cũng lỗi: " + fallbackEx.getMessage());
                return new ArrayList<>();
            }
        }
    }

    // ✅ Lấy giao dịch theo user với JOIN FETCH
    public List<Transaction> getTransactionsByUser(User user) {
        try {
            return transactionRepository.findByCreatedByWithDetails(user);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy giao dịch theo user: " + e.getMessage());
            return transactionRepository.findByCreatedBy(user);
        }
    }

    // ✅ Lấy giao dịch theo nhóm và trạng thái - SỬ DỤNG JOIN FETCH
    public List<Transaction> getTransactionsByGroupAndStatus(Long groupId, String status) {
        try {
            if (groupId == null && (status == null || status.isEmpty())) {
                return getAllTransactions();
            } else if (groupId == null) {
                return transactionRepository.findByStatusWithDetails(status);
            } else if (status == null || status.isEmpty()) {
                return getTransactionsByGroup(groupId);
            } else {
                List<Transaction> transactions = transactionRepository.findByGroupIdAndStatusWithDetails(groupId, status);
                System.out.println("✅ Loaded " + transactions.size() + " transactions for group " + groupId + " with status " + status + " (JOIN FETCH)");
                return transactions;
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi lấy giao dịch theo filter: " + e.getMessage());
            e.printStackTrace();
            try {
                if (groupId != null && status != null && !status.isEmpty()) {
                    return transactionRepository.findByGroupIdAndStatus(groupId, status);
                } else if (groupId == null && status != null && !status.isEmpty()) {
                    return transactionRepository.findByStatus(status);
                } else if (groupId != null) {
                    return transactionRepository.findByGroupId(groupId);
                } else {
                    return transactionRepository.findAll();
                }
            } catch (Exception fallbackEx) {
                return new ArrayList<>();
            }
        }
    }

    // ✅ Tạo giao dịch mới (đơn giản)
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getStatus() == null || transaction.getStatus().isEmpty()) {
            transaction.setStatus("PENDING");
        }
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }
        
        // ĐẢM BẢO CATEGORY KHÔNG NULL
        if (transaction.getCategory() == null) {
            Optional<Category> defaultCategory = categoryRepository.findById(4L);
            if (defaultCategory.isPresent()) {
                transaction.setCategory(defaultCategory.get());
                System.out.println("✅ Set category mặc định: " + defaultCategory.get().getName());
            }
        }
        
        return transactionRepository.save(transaction);
    }

    // ✅ Tạo giao dịch với nhóm - FIX LỖI CATEGORY NULL
    @Transactional
    public Transaction createTransactionWithGroup(Transaction transaction, Group group, Transaction.TransactionType type) {
        try {
            // Validate dữ liệu
            if (group == null) {
                throw new IllegalArgumentException("Nhóm không được để trống");
            }
            if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Số tiền phải lớn hơn 0");
            }
            if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Mô tả không được để trống");
            }

            // ĐẢM BẢO CATEGORY KHÔNG NULL - KIỂM TRA TRƯỚC KHI LƯU
            if (transaction.getCategory() == null) {
                Optional<Category> defaultCategory = categoryRepository.findById(4L);
                if (defaultCategory.isPresent()) {
                    transaction.setCategory(defaultCategory.get());
                    System.out.println("✅ TransactionService: Set category mặc định - " + defaultCategory.get().getName());
                } else {
                    System.out.println("⚠️ TransactionService: Không tìm thấy category id=4");
                }
            }

            // Set thông tin transaction (KHÔNG GHI ĐÈ CATEGORY)
            transaction.setGroup(group);
            transaction.setType(type);
            
            // Chỉ set nếu chưa có
            if (transaction.getStatus() == null || transaction.getStatus().isEmpty()) {
                transaction.setStatus("PENDING");
            }
            if (transaction.getDate() == null) {
                transaction.setDate(LocalDateTime.now());
            }
            if (transaction.getCreatedDate() == null) {
                transaction.setCreatedDate(LocalDateTime.now());
            }
            transaction.setUpdatedDate(LocalDateTime.now());

            // LƯU TRANSACTION VÀO DATABASE
            Transaction savedTransaction = transactionRepository.save(transaction);
            
            // Flush để đảm bảo data được commit ngay
            transactionRepository.flush();
            
            System.out.println("✅ Đã lưu giao dịch vào database:");
            System.out.println("   - ID: " + savedTransaction.getId());
            System.out.println("   - Type: " + type);
            System.out.println("   - Amount: " + savedTransaction.getAmount());
            System.out.println("   - Group: " + group.getName());
            System.out.println("   - Description: " + savedTransaction.getDescription());
            System.out.println("   - Status: " + savedTransaction.getStatus());
            System.out.println("   - Category: " + (savedTransaction.getCategory() != null ? savedTransaction.getCategory().getName() : "NULL"));
            System.out.println("   - Date: " + savedTransaction.getDate());

            // Tạo contributions cho từng user trong nhóm
            if (group.getUsers() != null && !group.getUsers().isEmpty()) {
                BigDecimal amountPerUser = transaction.getAmount()
                    .divide(BigDecimal.valueOf(group.getUsers().size()), 2, RoundingMode.HALF_UP);
                
                for (User user : group.getUsers()) {
                    try {
                        Contribution contribution = new Contribution();
                        contribution.setUser(user);
                        contribution.setTransaction(savedTransaction);
                        contribution.setAmountRequired(amountPerUser);
                        contribution.setAmountContributed(BigDecimal.ZERO);
                        contribution.setStatus("PENDING");
                        contributionService.save(contribution);
                        
                        // Gửi thông báo chi tiết hơn
                        String message = createDetailedNotificationMessage(type, savedTransaction, group, amountPerUser, user);
                        notificationService.sendNotification(user, message);
                        
                    } catch (Exception e) {
                        System.err.println("❌ Lỗi khi tạo contribution cho user " + user.getEmail() + ": " + e.getMessage());
                    }
                }
                
                System.out.println("✅ Đã tạo " + group.getUsers().size() + " contributions và gửi thông báo");
            }

            return savedTransaction;
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tạo giao dịch: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo giao dịch: " + e.getMessage(), e);
        }
    }

    // ✅ Tạo nội dung thông báo chi tiết hơn
    private String createDetailedNotificationMessage(Transaction.TransactionType type, Transaction transaction, 
                                                   Group group, BigDecimal amountPerUser, User user) {
        String userName = user.getName() != null ? user.getName() : user.getEmail();
        String currentTime = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        if (type == Transaction.TransactionType.INCOME) {
            return String.format("""
                🏦 YÊU CẦU ĐÓNG TIỀN - %s
                
                📋 Mô tả: %s
                💰 Số tiền cần đóng: %,.0f₫
                👥 Nhóm: %s (%d thành viên)
                📅 Thời hạn: 24 giờ
                
                💳 THÔNG TIN CHUYỂN KHOẢN:
                🏛️ Ngân hàng: Techcombank
                📧 STK: 9966504911
                👤 Chủ TK: PHAM KHUONG DUY
                💬 Nội dung CK: %s-%s
                
                ⚠️ Lưu ý: Vui lòng chuyển khoản đúng nội dung để được xác nhận tự động!
                📞 Liên hệ admin nếu có thắc mắc.
                🔍 Mã giao dịch: #%d
                """, 
                group.getName(),
                transaction.getDescription(), 
                amountPerUser.doubleValue(),
                group.getName(),
                group.getUsers().size(),
                group.getName().replaceAll("\\s+", ""),
                userName.replaceAll("\\s+", ""),
                transaction.getId()
            );
        } else {
            return String.format("""
                💸 THÔNG BÁO CHI TIỀN - %s
                
                📋 Lý do chi: %s
                💰 Số tiền đã chi: %,.0f₫
                👥 Nhóm: %s
                📅 Thời gian: %s
                👤 Người tạo: %s
                
                ℹ️ Tiền đã được chi từ quỹ chung của nhóm.
                📊 Kiểm tra số dư quỹ tại: Quản lý tài chính > Lịch sử giao dịch
                🔍 Mã giao dịch: #%d
                """, 
                group.getName(),
                transaction.getDescription(), 
                transaction.getAmount().doubleValue(),
                group.getName(),
                currentTime,
                transaction.getCreatedBy().getName() != null ? 
                    transaction.getCreatedBy().getName() : transaction.getCreatedBy().getEmail(),
                transaction.getId()
            );
        }
    }

    // ✅ User đóng góp vào giao dịch
    @Transactional
    public void contributeToTransaction(Long transactionId, User user, BigDecimal amount) {
        Transaction transaction = findById(transactionId);
        Contribution contribution = contributionService.findByTransactionAndUser(transaction, user);
        
        if (contribution == null) {
            throw new IllegalArgumentException("Không tìm thấy đóng góp cho user này.");
        }

        contribution.setAmountContributed(contribution.getAmountContributed().add(amount));
        if (contribution.getAmountContributed().compareTo(contribution.getAmountRequired()) >= 0) {
            contribution.setStatus("COMPLETED");
        }
        contributionService.save(contribution);

        checkAndUpdateTransactionStatus(transaction);
    }

    // ✅ Kiểm tra và cập nhật status transaction
    private void checkAndUpdateTransactionStatus(Transaction transaction) {
        List<Contribution> contributions = contributionService.findByTransaction(transaction);
        boolean allCompleted = contributions.stream()
            .allMatch(c -> "COMPLETED".equals(c.getStatus()));
        
        if (allCompleted) {
            transaction.setStatus("COMPLETED");
            transactionRepository.save(transaction);
            System.out.println("✅ Giao dịch " + transaction.getId() + " đã hoàn thành");
        }
    }

    // ✅ Lấy giao dịch theo ID
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giao dịch với ID: " + id));
    }

    // ✅ Cập nhật giao dịch
    @Transactional
    public Transaction updateTransaction(Transaction transaction) {
        Transaction existing = findById(transaction.getId());
        
        existing.setDescription(transaction.getDescription());
        existing.setAmount(transaction.getAmount());
        if (transaction.getGroup() != null) {
            existing.setGroup(transaction.getGroup());
        }
        if (transaction.getCategory() != null) {
            existing.setCategory(transaction.getCategory());
        }
        
        Transaction updated = transactionRepository.save(existing);
        System.out.println("✅ Đã cập nhật giao dịch " + updated.getId());
        return updated;
    }

    // ✅ Cập nhật trạng thái giao dịch
    @Transactional
    public Transaction updateStatus(Long id, String status) {
        Transaction tx = findById(id);
        String oldStatus = tx.getStatus();
        tx.setStatus(status);
        Transaction updated = transactionRepository.save(tx);
        
        System.out.println("✅ Đã cập nhật trạng thái giao dịch " + id + " từ " + oldStatus + " thành " + status);
        return updated;
    }

    // ✅ Duyệt giao dịch (COMPLETED)
    @Transactional
    public Transaction approveTransaction(Long id) {
        return updateStatus(id, "COMPLETED");
    }

    // ✅ Hủy giao dịch
    @Transactional
    public Transaction cancelTransaction(Long id) {
        return updateStatus(id, "CANCELLED");
    }

    // ✅ Xóa giao dịch
    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Không thể xóa. Giao dịch không tồn tại với ID: " + id);
        }
        
        Transaction transaction = findById(id);
        List<Contribution> contributions = contributionService.findByTransaction(transaction);
        for (Contribution contribution : contributions) {
            contributionService.delete(contribution);
        }
        
        transactionRepository.deleteById(id);
        System.out.println("✅ Đã xóa giao dịch " + id + " và " + contributions.size() + " contributions");
    }

    // ✅ Tính tổng thu theo nhóm
    public BigDecimal getTotalIncomeByGroup(Long groupId) {
        try {
            if (groupId == null) {
                BigDecimal total = transactionRepository.getTotalIncomeAll();
                System.out.println("✅ Tổng thu tất cả nhóm: " + total);
                return total != null ? total : BigDecimal.ZERO;
            }
            
            BigDecimal total = transactionRepository.getTotalIncomeByGroup(groupId);
            System.out.println("✅ Tổng thu nhóm " + groupId + ": " + total);
            return total != null ? total : BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính tổng thu: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ✅ Tính tổng chi theo nhóm
    public BigDecimal getTotalExpenseByGroup(Long groupId) {
        try {
            if (groupId == null) {
                BigDecimal total = transactionRepository.getTotalExpenseAll();
                System.out.println("✅ Tổng chi tất cả nhóm: " + total);
                return total != null ? total : BigDecimal.ZERO;
            }
            
            BigDecimal total = transactionRepository.getTotalExpenseByGroup(groupId);
            System.out.println("✅ Tổng chi nhóm " + groupId + ": " + total);
            return total != null ? total : BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính tổng chi: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // ✅ Tính số dư theo nhóm
    public BigDecimal getBalanceByGroup(Long groupId) {
        try {
            BigDecimal totalIncome = getTotalIncomeByGroup(groupId);
            BigDecimal totalExpense = getTotalExpenseByGroup(groupId);
            BigDecimal balance = totalIncome.subtract(totalExpense);
            
            System.out.println("✅ Số dư nhóm " + groupId + ": " + balance + " (Thu: " + totalIncome + " - Chi: " + totalExpense + ")");
            return balance;
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tính số dư: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    // ✅ Debug method - kiểm tra data trong database
    public void debugTransactions() {
        System.out.println("🔍 DEBUG: Kiểm tra transactions trong database...");
        
        try {
            List<Transaction> all = transactionRepository.findAllForDebug();
            System.out.println("📊 Tổng số transactions (JOIN FETCH): " + all.size());
            
            for (Transaction tx : all) {
                System.out.println(String.format("   ID: %d | Type: %s | Amount: %s | Group: %s | Status: %s | Date: %s | CreatedBy: %s", 
                    tx.getId(), 
                    tx.getType(), 
                    tx.getAmount(), 
                    tx.getGroup() != null ? tx.getGroup().getName() : "NULL",
                    tx.getStatus(),
                    tx.getDate(),
                    tx.getCreatedBy() != null ? tx.getCreatedBy().getEmail() : "NULL"
                ));
            }
        } catch (Exception e) {
            System.err.println("❌ Lỗi JOIN FETCH debug, fallback to findAll(): " + e.getMessage());
            
            List<Transaction> all = transactionRepository.findAll();
            System.out.println("📊 Tổng số transactions (findAll): " + all.size());
            
            for (Transaction tx : all) {
                System.out.println(String.format("   ID: %d | Type: %s | Amount: %s | Status: %s | Date: %s", 
                    tx.getId(), 
                    tx.getType(), 
                    tx.getAmount(), 
                    tx.getStatus(),
                    tx.getDate()
                ));
            }
        }
    }

    // ✅ Kiểm tra kết nối database
    public boolean isDatabaseConnected() {
        try {
            long count = transactionRepository.count();
            System.out.println("✅ Database connected. Total transactions: " + count);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Refresh cache và reload data
    public void refreshTransactionCache() {
        System.out.println("🔄 Refreshing transaction cache...");
        debugTransactions();
    }
}