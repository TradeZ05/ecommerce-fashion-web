package com.ngonlanh.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private String fullName;
    private String username;
    private String productName;
}