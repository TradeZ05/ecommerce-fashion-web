package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Thông tin giao hàng lưu cứng vào đơn (phòng trường hợp sau này user đổi địa chỉ thì đơn cũ không bị ảnh hưởng)
    private String receiverName;
    private String receiverPhone;
    private String shippingAddress;
    
    private Double totalAmount; // Tổng tiền
    
    // Trạng thái đơn: PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    private String status = "PENDING"; 
    
    private LocalDateTime orderDate = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}