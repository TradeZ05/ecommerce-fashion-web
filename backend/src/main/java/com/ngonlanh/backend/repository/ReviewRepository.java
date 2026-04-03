package com.ngonlanh.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ngonlanh.backend.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Integer productId); 
    Page<Review> findByProductId(Integer productId, Pageable pageable);
    boolean existsByUserIdAndProductId(Long userId, Integer productId);
}