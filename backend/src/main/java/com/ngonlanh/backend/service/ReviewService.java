package com.ngonlanh.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngonlanh.backend.dto.ReviewRequest;
import com.ngonlanh.backend.dto.ReviewResponse;
import com.ngonlanh.backend.entity.Product;
import com.ngonlanh.backend.entity.Review;
import com.ngonlanh.backend.entity.User;
import com.ngonlanh.backend.repository.OrderDetailRepository;
import com.ngonlanh.backend.repository.ProductRepository;
import com.ngonlanh.backend.repository.ReviewRepository;
import com.ngonlanh.backend.repository.UserRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Lấy danh sách review theo món ăn có hỗ trợ PHÂN TRANG và chuyển sang DTO.
     */
    public Page<ReviewResponse> getReviewsByProduct(Integer productId, Pageable pageable) {
        //
        Page<Review> reviewPage = reviewRepository.findByProductId(productId, pageable);
        return reviewPage.map(this::mapToResponse);
    }

    /**
     * Tạo review mới với đầy đủ các bước kiểm tra bảo mật và nghiệp vụ.
     */
    @Transactional // Đảm bảo tính nhất quán dữ liệu giữa Review và Product
    public ReviewResponse createReview(String username, ReviewRequest request) {
        // 1. Tìm user đang thực hiện đánh giá
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Kiểm tra xem khách hàng đã mua món này và đơn hàng đã DELIVERED chưa
        boolean hasBought = orderDetailRepository.existsByOrder_User_IdAndProduct_IdAndOrder_Status(
                user.getId(), request.getProductId(), "DELIVERED"
        );

        if (!hasBought) {
            throw new RuntimeException("Bạn chưa mua món này hoặc đơn hàng chưa hoàn thành, không thể đánh giá!");
        }

        // 3. CHỐNG SPAM: Kiểm tra nếu user đã đánh giá sản phẩm này chưa
        // (Lưu ý: Bạn cần thêm hàm này vào ReviewRepository mới chạy được)
        if (reviewRepository.existsByUserIdAndProductId(user.getId(), request.getProductId())) {
            throw new RuntimeException("Bạn đã đánh giá sản phẩm này rồi!");
        }

        // 4. Tìm sản phẩm cần đánh giá
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn"));

        // 5. Khởi tạo và lưu đánh giá mới
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now()); 

        Review savedReview = reviewRepository.save(review);

        // 6. Cập nhật lại điểm đánh giá trung bình cho sản phẩm
        updateProductRating(product);

        return mapToResponse(savedReview);
    }

    /**
     * Tính toán lại điểm trung bình từ tất cả review của món ăn.
     */
    private void updateProductRating(Product product) {
        // Lấy tất cả review của món ăn để tính điểm
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        
        if (reviews.isEmpty()) return;

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        
        // Làm tròn 1 chữ số thập phân
        double roundedAverage = Math.round(average * 10.0) / 10.0;
        
        // [QUAN TRỌNG] Lưu điểm mới vào Product Entity
        // Bạn cần thêm trường "averageRating" vào class Product.java mới dùng được dòng dưới
        product.setAverageRating(roundedAverage); 
        
        productRepository.save(product);
    }

    /**
     * Chuyển đổi sang DTO để bảo mật thông tin nhạy cảm
     */
    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        
        if (review.getUser() != null) {
            response.setFullName(review.getUser().getFullName());
            response.setUsername(review.getUser().getUsername());
        }
        
        if (review.getProduct() != null) {
            response.setProductName(review.getProduct().getName());
        }
        
        return response;
    }
}