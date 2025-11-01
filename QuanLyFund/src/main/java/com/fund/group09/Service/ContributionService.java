package com.fund.group09.Service;

import com.fund.group09.Model.Contribution;
import com.fund.group09.Model.Transaction;
import com.fund.group09.Model.User;
import com.fund.group09.Repository.ContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContributionService {

    @Autowired
    private ContributionRepository contributionRepository;

    // ✅ Lưu contribution
    @Transactional
    public Contribution save(Contribution contribution) {
        return contributionRepository.save(contribution);
    }

    // ✅ Tìm contribution theo ID
    public Contribution findById(Long id) {
        return contributionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy contribution với ID: " + id));
    }

    // ✅ Tìm contribution theo transaction
    public List<Contribution> findByTransaction(Transaction transaction) {
        return contributionRepository.findByTransaction(transaction);
    }

    // ✅ Tìm contribution theo transaction và user
    public Contribution findByTransactionAndUser(Transaction transaction, User user) {
        return contributionRepository.findByTransactionAndUser(transaction, user);
    }

    // ✅ Tìm tất cả contribution của user
    public List<Contribution> findByUser(User user) {
        return contributionRepository.findByUser(user);
    }

    // ✅ Tìm contribution theo trạng thái
    public List<Contribution> findByStatus(String status) {
        return contributionRepository.findByStatus(status);
    }

    // ✅ Tìm contribution của user theo trạng thái
    public List<Contribution> findByUserAndStatus(User user, String status) {
        return contributionRepository.findByUserAndStatus(user, status);
    }

    // ✅ Lấy tất cả contribution
    public List<Contribution> findAll() {
        return contributionRepository.findAll();
    }

    // ✅ Kiểm tra contribution tồn tại
    public boolean existsById(Long id) {
        return contributionRepository.existsById(id);
    }

    // ✅ Đếm số lượng contribution
    public long count() {
        return contributionRepository.count();
    }

    // ✅ Đếm contribution theo trạng thái
    public long countByStatus(String status) {
        return contributionRepository.countByStatus(status);
    }

    // ✅ Đếm contribution của user
    public long countByUser(User user) {
        return contributionRepository.countByUser(user);
    }

    // ✅ Cập nhật contribution
    @Transactional
    public Contribution update(Contribution contribution) {
        if (contribution.getId() == null) {
            throw new IllegalArgumentException("ID contribution không được null khi cập nhật");
        }
        
        Contribution existing = findById(contribution.getId());
        existing.setAmountContributed(contribution.getAmountContributed());
        existing.setStatus(contribution.getStatus());
        
        return contributionRepository.save(existing);
    }

    // ✅ Cập nhật số tiền đóng góp
    @Transactional
    public Contribution updateAmountContributed(Long contributionId, BigDecimal newAmount) {
        Contribution contribution = findById(contributionId);
        contribution.setAmountContributed(newAmount);
        
        // Tự động cập nhật status nếu đủ tiền
        if (newAmount.compareTo(contribution.getAmountRequired()) >= 0) {
            contribution.setStatus("COMPLETED");
        } else {
            contribution.setStatus("PENDING");
        }
        
        return contributionRepository.save(contribution);
    }

    // ✅ Cập nhật trạng thái contribution
    @Transactional
    public Contribution updateStatus(Long contributionId, String status) {
        Contribution contribution = findById(contributionId);
        contribution.setStatus(status);
        return contributionRepository.save(contribution);
    }

    // ✅ Đánh dấu hoàn thành
    @Transactional
    public Contribution markAsCompleted(Long contributionId) {
        return updateStatus(contributionId, "COMPLETED");
    }

    // ✅ Đánh dấu hủy
    @Transactional
    public Contribution markAsCancelled(Long contributionId) {
        return updateStatus(contributionId, "CANCELLED");
    }

    // ✅ Xóa contribution theo ID
    @Transactional
    public void deleteById(Long id) {
        if (!contributionRepository.existsById(id)) {
            throw new IllegalArgumentException("Không thể xóa. Contribution không tồn tại với ID: " + id);
        }
        contributionRepository.deleteById(id);
        System.out.println("✅ Đã xóa contribution với ID: " + id);
    }

    // ✅ Xóa contribution object (cho TransactionService)
    @Transactional
    public void delete(Contribution contribution) {
        if (contribution == null || contribution.getId() == null) {
            throw new IllegalArgumentException("Contribution không hợp lệ để xóa");
        }
        contributionRepository.delete(contribution);
        System.out.println("✅ Đã xóa contribution với ID: " + contribution.getId());
    }

    // ✅ Xóa tất cả contribution của transaction
    @Transactional
    public void deleteByTransaction(Transaction transaction) {
        List<Contribution> contributions = findByTransaction(transaction);
        contributionRepository.deleteAll(contributions);
        System.out.println("✅ Đã xóa " + contributions.size() + " contributions của transaction " + transaction.getId());
    }

    // ✅ Xóa tất cả contribution của user
    @Transactional
    public void deleteByUser(User user) {
        List<Contribution> contributions = findByUser(user);
        contributionRepository.deleteAll(contributions);
        System.out.println("✅ Đã xóa " + contributions.size() + " contributions của user " + user.getEmail());
    }

    // ✅ Kiểm tra user đã đóng đủ tiền chưa
    public boolean isUserFullyContributed(Transaction transaction, User user) {
        Contribution contribution = findByTransactionAndUser(transaction, user);
        if (contribution == null) return false;
        
        return contribution.getAmountContributed()
                .compareTo(contribution.getAmountRequired()) >= 0;
    }

    // ✅ Tính tổng số tiền user đã đóng
    public BigDecimal getTotalContributedByUser(User user) {
        List<Contribution> contributions = findByUser(user);
        return contributions.stream()
                .map(Contribution::getAmountContributed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ Tính tổng số tiền cần đóng của user
    public BigDecimal getTotalRequiredByUser(User user) {
        List<Contribution> contributions = findByUser(user);
        return contributions.stream()
                .map(Contribution::getAmountRequired)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ Tính số tiền còn thiếu của user
    public BigDecimal getRemainingAmountByUser(User user) {
        BigDecimal totalRequired = getTotalRequiredByUser(user);
        BigDecimal totalContributed = getTotalContributedByUser(user);
        return totalRequired.subtract(totalContributed);
    }

    // ✅ Lấy danh sách contribution chưa hoàn thành của user
    public List<Contribution> getPendingContributionsByUser(User user) {
        return findByUserAndStatus(user, "PENDING");
    }

    // ✅ Lấy danh sách contribution đã hoàn thành của user
    public List<Contribution> getCompletedContributionsByUser(User user) {
        return findByUserAndStatus(user, "COMPLETED");
    }
}