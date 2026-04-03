package com.ngonlanh.backend.controller;

import jakarta.validation.Valid; // Thêm import này để kích hoạt Validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.ngonlanh.backend.dto.ReviewRequest;
import com.ngonlanh.backend.dto.ReviewResponse;
import com.ngonlanh.backend.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("*") 
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * API 1: Trả về danh sách review của 1 món ăn có hỗ trợ PHÂN TRANG.
     * Mặc định lấy trang 0, mỗi trang 5 đánh giá, sắp xếp từ mới nhất đến cũ nhất.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getReviews(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        
        // Tạo đối tượng phân trang dựa trên ID giảm dần
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        Page<ReviewResponse> reviews = reviewService.getReviewsByProduct(productId, pageable);
        return ResponseEntity.ok(reviews);
    }

    /**
     * API 2: Cho phép User gửi review mới.
     * Sử dụng @Valid để kiểm tra dữ liệu từ ReviewRequest trước khi xử lý.
     */
    @PostMapping
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewRequest request) { // Thêm @Valid ở đây
        try {
            // Lấy thông tin xác thực từ Security Context
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Gọi Service thực hiện lưu và trả về dữ liệu đã được lọc qua DTO
            ReviewResponse savedReview = reviewService.createReview(username, request);
            return ResponseEntity.ok(savedReview);

        } catch (RuntimeException e) {
            // Trả về lỗi nghiệp vụ (ví dụ: chưa mua hàng hoặc đã đánh giá rồi)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi hệ thống không lường trước
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi hệ thống");
        }
    }
}