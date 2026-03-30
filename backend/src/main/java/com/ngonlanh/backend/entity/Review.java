package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer rating; // Số sao đánh giá (Ví dụ: 1 đến 5)

    @Column(columnDefinition = "TEXT")
    private String comment; // Nội dung bình luận

    private LocalDateTime createdAt = LocalDateTime.now(); // Thời gian đánh giá
}