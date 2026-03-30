package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    
    // BẮT BUỘC: Phải lưu giá tiền tại thời điểm mua (phòng trường hợp sau này món ăn tăng/giảm giá)
    private Double unitPrice; 
}