package com.fund.group09.Repository;

import com.fund.group09.Model.Contribution;
import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    
    // ✅ Tìm contribution theo transaction
    List<Contribution> findByTransaction(Transaction transaction);
    
    // ✅ Tìm contribution theo transaction và user
    Contribution findByTransactionAndUser(Transaction transaction, User user);
    
    // ✅ Tìm contribution theo user
    List<Contribution> findByUser(User user);
    
    // ✅ Tìm contribution theo status
    List<Contribution> findByStatus(String status);
    
    // ✅ Tìm contribution theo user và status
    List<Contribution> findByUserAndStatus(User user, String status);
    
    // ✅ Tìm contribution theo transaction và status
    List<Contribution> findByTransactionAndStatus(Transaction transaction, String status);
    
    // ✅ Đếm contribution theo status
    Long countByStatus(String status);
    
    // ✅ Đếm contribution theo user
    Long countByUser(User user);
    
    // ✅ Đếm contribution theo transaction
    Long countByTransaction(Transaction transaction);
    
    // ✅ Đếm contribution theo user và status
    Long countByUserAndStatus(User user, String status);
    
    // ✅ Kiểm tra user có contribution trong transaction không
    boolean existsByTransactionAndUser(Transaction transaction, User user);
    
    // ✅ Tính tổng số tiền đã đóng góp của user
    @Query("SELECT SUM(c.amountContributed) FROM Contribution c WHERE c.user = :user")
    BigDecimal sumAmountContributedByUser(@Param("user") User user);
    
    // ✅ Tính tổng số tiền cần đóng của user
    @Query("SELECT SUM(c.amountRequired) FROM Contribution c WHERE c.user = :user")
    BigDecimal sumAmountRequiredByUser(@Param("user") User user);
    
    // ✅ Tính tổng số tiền đã đóng góp cho transaction
    @Query("SELECT SUM(c.amountContributed) FROM Contribution c WHERE c.transaction = :transaction")
    BigDecimal sumAmountContributedByTransaction(@Param("transaction") Transaction transaction);
    
    // ✅ Tính tổng số tiền cần đóng cho transaction
    @Query("SELECT SUM(c.amountRequired) FROM Contribution c WHERE c.transaction = :transaction")
    BigDecimal sumAmountRequiredByTransaction(@Param("transaction") Transaction transaction);
    
    // ✅ Tìm contribution theo user và status PENDING
    @Query("SELECT c FROM Contribution c WHERE c.user = :user AND c.status = 'PENDING'")
    List<Contribution> findPendingContributionsByUser(@Param("user") User user);
    
    // ✅ Tìm contribution theo user và status COMPLETED
    @Query("SELECT c FROM Contribution c WHERE c.user = :user AND c.status = 'COMPLETED'")
    List<Contribution> findCompletedContributionsByUser(@Param("user") User user);
    
    // ✅ Tìm contribution theo transaction và status PENDING
    @Query("SELECT c FROM Contribution c WHERE c.transaction = :transaction AND c.status = 'PENDING'")
    List<Contribution> findPendingContributionsByTransaction(@Param("transaction") Transaction transaction);
    
    // ✅ Tìm contribution theo transaction và status COMPLETED
    @Query("SELECT c FROM Contribution c WHERE c.transaction = :transaction AND c.status = 'COMPLETED'")
    List<Contribution> findCompletedContributionsByTransaction(@Param("transaction") Transaction transaction);
    
    // ✅ Kiểm tra tất cả contribution của transaction đã hoàn thành chưa
    @Query("SELECT COUNT(c) = 0 FROM Contribution c WHERE c.transaction = :transaction AND c.status != 'COMPLETED'")
    boolean isTransactionFullyContributed(@Param("transaction") Transaction transaction);
    
    // ✅ Tìm contribution theo số tiền lớn hơn
    @Query("SELECT c FROM Contribution c WHERE c.amountContributed >= :amount")
    List<Contribution> findByAmountContributedGreaterThanEqual(@Param("amount") BigDecimal amount);
    
    // ✅ Tìm contribution theo số tiền nhỏ hơn
    @Query("SELECT c FROM Contribution c WHERE c.amountContributed < :amount")
    List<Contribution> findByAmountContributedLessThan(@Param("amount") BigDecimal amount);
    
    // ✅ Tìm contribution chưa đóng đủ tiền
    @Query("SELECT c FROM Contribution c WHERE c.amountContributed < c.amountRequired")
    List<Contribution> findIncompleteContributions();
    
    // ✅ Tìm contribution đã đóng đủ tiền
    @Query("SELECT c FROM Contribution c WHERE c.amountContributed >= c.amountRequired")
    List<Contribution> findCompleteContributions();
    
    // ✅ Tìm contribution theo user chưa đóng đủ tiền
    @Query("SELECT c FROM Contribution c WHERE c.user = :user AND c.amountContributed < c.amountRequired")
    List<Contribution> findIncompleteContributionsByUser(@Param("user") User user);
    
    // ✅ Tìm contribution theo transaction ID
    @Query("SELECT c FROM Contribution c WHERE c.transaction.id = :transactionId")
    List<Contribution> findByTransactionId(@Param("transactionId") Long transactionId);
    
    // ✅ Tìm contribution theo user ID
    @Query("SELECT c FROM Contribution c WHERE c.user.id = :userId")
    List<Contribution> findByUserId(@Param("userId") Long userId);
    
    // ✅ Xóa contribution theo transaction
    void deleteByTransaction(Transaction transaction);
    
    // ✅ Xóa contribution theo user
    void deleteByUser(User user);
    
    // ✅ Xóa contribution theo transaction và user
    void deleteByTransactionAndUser(Transaction transaction, User user);
}