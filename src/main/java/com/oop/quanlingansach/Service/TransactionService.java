package com.oop.quanlingansach.Service;

import com.oop.quanlingansach.Model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    // Lấy danh sách giao dịch theo userId
    public List<Transaction> getTransactionsByUserId(Long userId) {
        // TODO: Triển khai truy vấn từ database
        return List.of(); // Trả về danh sách rỗng (demo)
    }

    // Lấy chi tiết giao dịch theo id
    public Optional<Transaction> getTransactionById(Long id) {
        // TODO: Triển khai truy vấn từ database
        return Optional.empty(); // Trả về rỗng (demo)
    }

    // Có thể bổ sung thêm các phương thức thêm, sửa, xóa giao dịch ở đây
}