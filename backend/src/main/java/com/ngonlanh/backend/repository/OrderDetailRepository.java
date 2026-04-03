package com.ngonlanh.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ngonlanh.backend.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
    // Câu lệnh này dịch ra là: Kiểm tra xem có tồn tại (exists) chi tiết đơn hàng nào
    // mà ID của người dùng (Order_User_Id) VÀ ID sản phẩm (Product_Id) VÀ trạng thái đơn (Order_Status) khớp với dữ liệu truyền vào không.
    boolean existsByOrder_User_IdAndProduct_IdAndOrder_Status(Long userId, Integer productId, String status);
}