package com.ngonlanh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ 1-1: Một khách hàng chỉ có 1 giỏ hàng duy nhất
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // cascade = ALL & orphanRemoval = true: Khi xóa giỏ hàng, tự động xóa hết các món bên trong
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
}