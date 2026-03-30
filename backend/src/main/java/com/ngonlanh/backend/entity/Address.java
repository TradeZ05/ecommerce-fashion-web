package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ N-1: Nhiều địa chỉ thuộc về 1 User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String receiverName; // Tên người nhận (có thể khác tên tài khoản)
    private String phone;        // Số điện thoại liên hệ khi giao
    
    @Column(columnDefinition = "TEXT")
    private String detailedAddress; // Địa chỉ cụ thể (Số nhà, đường, phường, quận...)

    private boolean isDefault = false; // Cờ đánh dấu địa chỉ mặc định
}