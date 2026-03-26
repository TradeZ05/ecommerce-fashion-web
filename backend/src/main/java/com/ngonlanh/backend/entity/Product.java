package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private Double price;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Khai báo mối quan hệ: Nhiều Sản phẩm thuộc về 1 Danh mục
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}