package com.ngonlanh.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @Min(value = 1, message = "Đánh giá thấp nhất là 1 sao")
    @Max(value = 5, message = "Đánh giá cao nhất là 5 sao")
    private int rating;

    @NotBlank(message = "Nội dung đánh giá không được để trống")
    @Size(max = 500, message = "Nội dung không được quá 500 ký tự")
    private String comment;
}