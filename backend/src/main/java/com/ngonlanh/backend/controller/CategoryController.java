package com.ngonlanh.backend.controller;

import com.ngonlanh.backend.entity.Category;
import com.ngonlanh.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. Cửa GET: Lấy toàn bộ danh mục trả về cho Frontend
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. Cửa POST: Nhận dữ liệu từ Frontend để lưu xuống Database
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        // Hàm save() sẽ tự động tạo câu lệnh INSERT INTO... giống hệt bạn gõ lúc nãy
        return categoryRepository.save(category);
    }

    // Xóa danh mục theo ID
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
    }

    // Cập nhật danh mục theo ID
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category categoryDetails) {
        // 1. Tìm danh mục cũ trong DB
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục này!"));
        
        // 2. Ghi đè thông tin mới
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        
        // 3. Lưu lại
        return categoryRepository.save(category);
    }
}