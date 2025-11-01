package com.fund.group09.Repository;

import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // ✅ Lấy tất cả với JOIN FETCH để tránh lazy loading
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "ORDER BY t.date DESC")
    List<Transaction> findAllWithDetails();
    
    // ✅ Tìm giao dịch theo người tạo với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "WHERE t.createdBy = :createdBy " +
           "ORDER BY t.date DESC")
    List<Transaction> findByCreatedByWithDetails(@Param("createdBy") User createdBy);
    
    // Tìm giao dịch theo người tạo (method gốc)
    List<Transaction> findByCreatedBy(User createdBy);
    
    // ✅ Tìm giao dịch theo trạng thái với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.status = :status " +
           "ORDER BY t.date DESC")
    List<Transaction> findByStatusWithDetails(@Param("status") String status);
    
    // Tìm giao dịch theo trạng thái (method gốc)
    List<Transaction> findByStatus(String status);
    
    // ✅ Tìm giao dịch theo nhóm ID với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.group.id = :groupId " +
           "ORDER BY t.date DESC")
    List<Transaction> findByGroupIdWithDetails(@Param("groupId") Long groupId);
    
    // Tìm giao dịch theo nhóm ID (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId ORDER BY t.date DESC")
    List<Transaction> findByGroupId(@Param("groupId") Long groupId);
    
    // ✅ Tìm giao dịch theo nhóm ID và trạng thái với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.group.id = :groupId AND t.status = :status " +
           "ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndStatusWithDetails(@Param("groupId") Long groupId, @Param("status") String status);
    
    // Tìm giao dịch theo nhóm ID và trạng thái (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId AND t.status = :status ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndStatus(@Param("groupId") Long groupId, @Param("status") String status);
    
    // ✅ Tìm giao dịch theo loại với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.type = :type " +
           "ORDER BY t.date DESC")
    List<Transaction> findByTypeWithDetails(@Param("type") Transaction.TransactionType type);
    
    // Tìm giao dịch theo loại (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.type = :type ORDER BY t.date DESC")
    List<Transaction> findByType(@Param("type") Transaction.TransactionType type);
    
    // ✅ Tìm giao dịch theo nhóm và loại với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.group.id = :groupId AND t.type = :type " +
           "ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndTypeWithDetails(@Param("groupId") Long groupId, @Param("type") Transaction.TransactionType type);
    
    // Tìm giao dịch theo nhóm và loại (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId AND t.type = :type ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndType(@Param("groupId") Long groupId, @Param("type") Transaction.TransactionType type);
    
    // ✅ Tìm giao dịch theo nhóm, loại và trạng thái với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.group.id = :groupId AND t.type = :type AND t.status = :status " +
           "ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndTypeAndStatusWithDetails(@Param("groupId") Long groupId, 
                                                             @Param("type") Transaction.TransactionType type, 
                                                             @Param("status") String status);
    
    // Tìm giao dịch theo nhóm, loại và trạng thái (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId AND t.type = :type AND t.status = :status ORDER BY t.date DESC")
    List<Transaction> findByGroupIdAndTypeAndStatus(@Param("groupId") Long groupId, 
                                                   @Param("type") Transaction.TransactionType type, 
                                                   @Param("status") String status);
    
    // ✅ Đếm số giao dịch theo trạng thái
    Long countByStatus(String status);
    
    // ✅ Đếm số giao dịch theo nhóm
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.group.id = :groupId")
    Long countByGroupId(@Param("groupId") Long groupId);
    
    // ✅ Lấy giao dịch mới nhất của nhóm với JOIN FETCH
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u " +
           "WHERE t.group.id = :groupId " +
           "ORDER BY t.date DESC")
    List<Transaction> findByGroupIdOrderByDateDescWithDetails(@Param("groupId") Long groupId);
    
    // Lấy giao dịch mới nhất của nhóm (method gốc)
    @Query("SELECT t FROM Transaction t WHERE t.group.id = :groupId ORDER BY t.date DESC")
    List<Transaction> findByGroupIdOrderByDateDesc(@Param("groupId") Long groupId);
    
    // ✅ Thống kê tổng thu theo nhóm
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.group.id = :groupId AND t.type = 'INCOME'")
    BigDecimal getTotalIncomeByGroup(@Param("groupId") Long groupId);
    
    // ✅ Thống kê tổng chi theo nhóm
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.group.id = :groupId AND t.type = 'EXPENSE'")
    BigDecimal getTotalExpenseByGroup(@Param("groupId") Long groupId);
    
    // ✅ Thống kê tổng thu tất cả nhóm
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal getTotalIncomeAll();
    
    // ✅ Thống kê tổng chi tất cả nhóm
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal getTotalExpenseAll();
    
    // ✅ Kiểm tra có tồn tại giao dịch trong nhóm không
    @Query("SELECT COUNT(t) > 0 FROM Transaction t WHERE t.group.id = :groupId")
    boolean existsByGroupId(@Param("groupId") Long groupId);
    
    // ✅ Debug: Lấy tất cả transaction với thông tin đầy đủ
    @Query("SELECT t FROM Transaction t " +
           "LEFT JOIN FETCH t.group g " +
           "LEFT JOIN FETCH t.createdBy u")
    List<Transaction> findAllForDebug();
}